//
//  Copyright (C) 2001-2004 HorizonLive.com, Inc.  All Rights Reserved.
//  Copyright (C) 2001-2006 Constantin Kaplinsky.  All Rights Reserved.
//  Copyright (C) 2000 Tridia Corporation.  All Rights Reserved.
//  Copyright (C) 1999 AT&T Laboratories Cambridge.  All Rights Reserved.
//
//  This remoteInputStream free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 2 of the License, or
//  (at your option) any later version.
//
//  This software is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this software; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307,
//  USA.
//

//
// RfbProto.java
//

package com.coboltforge.dontmind.xbox.protocol;

import android.util.Log;

import com.coboltforge.dontmind.xbox.utils.DH;
import com.coboltforge.dontmind.xbox.utils.DesCipher;
import com.coboltforge.dontmind.xbox.security.RFBSecurityARD;
import com.coboltforge.dontmind.xbox.ui.VNCConn;
import com.coboltforge.dontmind.xbox.common.CapabilityInfo;
import com.coboltforge.dontmind.xbox.utils.CapsContainer;
import com.coboltforge.dontmind.xbox.utils.Utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

//- import java.awt.*;
//- import java.awt.event.*;
//- import java.util.zip.*;

/**
 * Access the RFB protocol through a socket.
 * <p>
 * This class has no knowledge of the android-specific UI; it sees framebuffer updates
 * and input events as defined in the RFB protocol.
 */
public class RfbProto {

    public final static int SecTypeTight = 16;

    // Supported encodings and pseudo-encodings
    public final static int EncodingRaw = 0;
    public final static int EncodingCopyRect = 1;
    public final static int EncodingRRE = 2;
    public final static int EncodingCoRRE = 4;
    public final static int EncodingHextile = 5;
    public final static int EncodingZlib = 6;
    public final static int EncodingTight = 7;
    public final static int EncodingZRLE = 16;
    public final static int EncodingCompressLevel0 = 0xFFFFFF00;
    public final static int EncodingQualityLevel0 = 0xFFFFFFE0;
    public final static int EncodingPointerPos = 0xFFFFFF18;
    public final static int EncodingLastRect = 0xFFFFFF20;
    public final static int EncodingNewFBSize = 0xFFFFFF21;

    // Contstants used in the Hextile decoder
    public final static int HextileRaw = 1;
    public final static int HextileBackgroundSpecified = 2;
    public final static int HextileForegroundSpecified = 4;
    public final static int HextileAnySubrects = 8;
    public final static int HextileSubrectsColoured = 16;
    public final static int SecTypeUltra34 = 0xfffffffa;
    public final static int SecTypeARD = 30;

    // Supported authentication types
    public final static int AuthNone = 1;
    public final static int AuthVNC = 2;
    public final static int AuthUnixLogin = 129;
    public final static int AuthUltra = 17;
    public final static int AuthARD = 30;

    public final static String SigAuthNone = "NOAUTH__";
    public final static String SigAuthVNC = "VNCAUTH_";
    public final static String SigAuthUnixLogin = "ULGNAUTH";

    // VNC authentication results
    public final static int VncAuthOK = 0;
    public final static int VncAuthFailed = 1;
    public final static int VncAuthTooMany = 2;

    // Server-to-client messages
    public final static int FramebufferUpdate = 0;
    public final static int SetColourMapEntries = 1;
    public final static int Bell = 2;
    public final static int ServerCutText = 3;
    public final static int TextChat = 11;
    public final static int EncodingXCursor = 0xFFFFFF10;
    public final static int EncodingRichCursor = 0xFFFFFF11;
    final static String TAG = "RfbProto";
    final static String
            versionMsg_3_3 = "RFB 003.003\n",
            versionMsg_3_7 = "RFB 003.007\n",
            versionMsg_3_8 = "RFB 003.008\n";
    // Vendor signatures: standard VNC/RealVNC, TridiaVNC, and TightVNC
    final static String
            StandardVendor = "STDV",
            TridiaVncVendor = "TRDV",
            TightVncVendor = "TGHT";
    // Security types
    final static int
            SecTypeInvalid = 0;
    final static int SecTypeNone = 1;
    final static int SecTypeVncAuth = 2;
    // Supported tunneling types
    final static int
            NoTunneling = 0;
    final static String
            SigNoTunneling = "NOTUNNEL";
    // Client-to-server messages
    final static int
            SetPixelFormat = 0,
            FixColourMapEntries = 1,
            SetEncodings = 2,
            FramebufferUpdateRequest = 3,
            KeyboardEvent = 4,
            PointerEvent = 5,
            ClientCutText = 6;
    final static String
            SigEncodingRaw = "RAW_____",
            SigEncodingCopyRect = "COPYRECT",
            SigEncodingRRE = "RRE_____",
            SigEncodingCoRRE = "CORRE___",
            SigEncodingHextile = "HEXTILE_",
            SigEncodingZlib = "ZLIB____",
            SigEncodingTight = "TIGHT___",
            SigEncodingZRLE = "ZRLE____",
            SigEncodingCompressLevel0 = "COMPRLVL",
            SigEncodingQualityLevel0 = "JPEGQLVL",
            SigEncodingXCursor = "X11CURSR",
            SigEncodingRichCursor = "RCHCURSR",
            SigEncodingPointerPos = "POINTPOS",
            SigEncodingLastRect = "LASTRECT",
            SigEncodingNewFBSize = "NEWFBSIZ";
    final static int MaxNormalEncoding = 255;
    // Contstants used in the Tight decoder
    final static int TightMinToCompress = 12;
    final static int
            TightExplicitFilter = 0x04,
            TightFill = 0x08,
            TightJpeg = 0x09,
            TightMaxSubencoding = 0x09,
            TightFilterCopy = 0x00,
            TightFilterPalette = 0x01,
            TightFilterGradient = 0x02;

    // Constants used for UltraVNC chat extension
    final static int CHAT_OPEN = -1;
    final static int CHAT_CLOSE = -2;
    final static int CHAT_FINISHED = -3;

    public DataInputStream remoteInputStream;
    //- SessionRecorder rec;
    public boolean inNormalProtocol = false;
    //
    // Constructor. Make TCP connection to RFB server.
    //
    public String desktopName;
    public int framebufferWidth;
    public int framebufferHeight;
    //
    // Select security type from the server's list (protocol versions 3.7/3.8).
    //
    public int copyRectSrcX;
    public int copyRectSrcY;


    //public OutputStream os;
    public OutputStream remoteOutputStream;
    // Protocol version and TightVNC-specific protocol options.
    public int serverMajor;
    public int serverMinor;
    public int clientMajor;
    public int clientMinor;
    //
    // Negotiate the authentication scheme.
    //
    public int updateNRects;
    //
    // Read security type from the server (protocol version 3.3).
    //
    public int updateRectX;
    public int updateRectY;
    public int updateRectW;
    public int updateRectH;
    public int updateRectEncoding;


    String host;
    //- VncViewer viewer;
    int port;
    Socket sock;
    DH dh;
    long dh_resp;


    // Java on UNIX does not call keyPressed() on some keys, for example
    // swedish keys To prevent our workaround to produce duplicate
    // keypresses on JVMs that actually works, keep track of if
    // keyPressed() for a "broken" key was called or not.
    boolean brokenKeyPressed = false;
    // This will be set to true on the first framebuffer update
    // containing Zlib-, ZRLE- or Tight-encoded data.
    boolean wereZlibUpdates = false;
    // This will be set to false if the startSession() was called after
    // we have received at least one Zlib-, ZRLE- or Tight-encoded
    // framebuffer update.
    boolean recordFromBeginning = true;
    // This fields are needed to show warnings about inefficiently saved
    // sessions only once per each saved session file.
    boolean zlibWarningShown;
    boolean tightWarningShown;
    // Before starting to record each saved session, we set this field
    // to 0, and increment on each framebuffer update. We don't flush
    // the SessionRecorder data into the file before the second update.
    // This allows us to write initial framebuffer update with zero
    // timestamp, to let the player show initial desktop before
    // playback.
    int numUpdatesInSession;
    // Measuring network throughput.
    boolean timing;
    long timeWaitedIn100us;
    long timedKbits;
    boolean protocolTightVNC;
    CapsContainer tunnelCaps, authCaps;
    CapsContainer serverMsgCaps, clientMsgCaps;
    CapsContainer encodingCaps;
    byte[] writeIntBuffer = new byte[4];
    int bitsPerPixel, depth;
    //
    // Read server's protocol version message
    //
    boolean bigEndian, trueColour;
    //
    // Write our protocol version message
    //
    int redMax, greenMax, blueMax, redShift, greenShift, blueShift;
    //
    // Perform "no authentication".
    //
    byte[] framebufferUpdateRequest = new byte[10];

    //
    // Perform standard VNC Authentication.
    //
    byte[] eventBuf = new byte[72];
    int eventBufLen;

    //
    // Read security result.
    // Throws an exception on authentication failure.
    //
    int oldModifiers = 0;

    //
    // Read the string describing the reason for a connection failure,
    // and throw an exception.
    //
    // If true, informs that the RFB socket was closed.
    private boolean closed;

    //-RfbProto(String h, int p, VncViewer v) throws IOException {
    public RfbProto(String h, int p) throws IOException {
        //- viewer = v;
        host = h;
        port = p;

    /*
    if (viewer.socketFactory == null) {
      sock = new Socket(host, port);
    } else {
      try {
	Class factoryClass = Class.forName(viewer.socketFactory);
	SocketFactory factory = (SocketFactory)factoryClass.newInstance();
	//- if (viewer.inAnApplet)
	//-   sock = factory.createSocket(host, port, viewer);
	//- else
	  sock = factory.createSocket(host, port, viewer.mainArgs);
    }
    catch(Exception e) {
	e.printStackTrace();
	throw new IOException(e.getMessage());
      }
    } */
        //+
        sock = new Socket(host, port);
        remoteInputStream = new DataInputStream(new BufferedInputStream(sock.getInputStream(),
                16384));
        remoteOutputStream = sock.getOutputStream();

        timing = false;
        timeWaitedIn100us = 5;
        timedKbits = 0;
    }

    public synchronized void close() {
        try {
            sock.close();
            closed = true;
            //- System.out.println("RFB socket closed");
            Log.v(TAG, "RFB socket closed");
            /*-
            if (rec != null) {
        	   rec.close();
        	   rec = null;

            } */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //
    // Initialize capability lists (TightVNC protocol extensions).
    //

    synchronized boolean closed() {
        return closed;
    }

    //
    // Setup tunneling (TightVNC protocol extensions)
    //

    public void readVersionMsg() throws Exception {

        byte[] b = new byte[12];

        readFully(b);

        if ((b[0] != 'R') || (b[1] != 'F') || (b[2] != 'B') || (b[3] != ' ')
                || (b[4] < '0') || (b[4] > '9') || (b[5] < '0') || (b[5] > '9')
                || (b[6] < '0') || (b[6] > '9') || (b[7] != '.')
                || (b[8] < '0') || (b[8] > '9') || (b[9] < '0') || (b[9] > '9')
                || (b[10] < '0') || (b[10] > '9') || (b[11] != '\n')) {
            Log.i(TAG, new String(b));
            throw new Exception("Host " + host + " port " + port +
                    " remoteInputStream not an RFB server");
        }

        serverMajor = (b[4] - '0') * 100 + (b[5] - '0') * 10 + (b[6] - '0');
        serverMinor = (b[8] - '0') * 100 + (b[9] - '0') * 10 + (b[10] - '0');

        if (serverMajor < 3) {
            throw new Exception("RFB server does not support protocol version 3");
        }
    }

    //
    // Negotiate authentication scheme (TightVNC protocol extensions)
    //

    public synchronized void writeVersionMsg() throws IOException {
        clientMajor = 3;
        if (serverMajor > 3 || serverMinor >= 8) {
            clientMinor = 8;
            remoteOutputStream.write(versionMsg_3_8.getBytes());
        } else if (serverMinor >= 7) {
            clientMinor = 7;
            remoteOutputStream.write(versionMsg_3_7.getBytes());
        } else {
            clientMinor = 3;
            remoteOutputStream.write(versionMsg_3_3.getBytes());
        }
        protocolTightVNC = false;
    }

    //
    // Read a capability list (TightVNC protocol extensions)
    //

    public int negotiateSecurity(int bitPref) throws Exception {
        return (clientMinor >= 7) ?
                selectSecurityType(bitPref) : readSecurityType(bitPref);
    }

    //
    // Write a 32-bit integer into the output stream.
    //

    int readSecurityType(int bitPref) throws Exception {
        int secType = remoteInputStream.readInt();

        switch (secType) {
            case SecTypeInvalid:
                readConnFailedReason();
                return SecTypeInvalid;    // should never be executed
            case SecTypeNone:
            case SecTypeVncAuth:
                return secType;
            case SecTypeUltra34:
                if ((bitPref & 1) == 1){
                    return secType;
                }
                throw new Exception("Username required.");
            default:
                throw new Exception("Unknown security type from RFB server: " + secType);
        }
    }

    int selectSecurityType(int bitPref) throws Exception {
        int secType = SecTypeInvalid;

        // Read the list of security types.
        int nSecTypes = remoteInputStream.readUnsignedByte();
        if (nSecTypes == 0) {
            readConnFailedReason();
            return SecTypeInvalid;    // should never be executed
        }
        byte[] secTypes = new byte[nSecTypes];
        readFully(secTypes);

        // Find out if the server supports TightVNC protocol extensions
        for (int i = 0; i < nSecTypes; i++) {
            if (secTypes[i] == SecTypeTight) {
                protocolTightVNC = true;
                remoteOutputStream.write(SecTypeTight);
                return SecTypeTight;
            }
        }

        // Find first supported security type.
        for (int i = 0; i < nSecTypes; i++) {
            if (secTypes[i] == SecTypeNone || secTypes[i] == SecTypeVncAuth || secTypes[i] == SecTypeARD) {
                secType = secTypes[i];
                break;
            }
        }

        if (secType == SecTypeInvalid) {
            throw new Exception("Server did not offer supported security type");
        } else {
            remoteOutputStream.write(secType);
        }

        return secType;
    }

    //
    // Write the client initialisation message
    //

    public void authenticateNone() throws Exception {
        if (clientMinor >= 8)
            readSecurityResult("No authentication");
    }


    //
    // Read the server initialisation message
    //

    public void authenticateVNC(String pw) throws Exception {
        byte[] challenge = new byte[16];
        readFully(challenge);

        if (pw.length() > 8){
            pw = pw.substring(0, 8);    // Truncate to 8 chars
        }

        // Truncate password on the first zero byte.
        int firstZero = pw.indexOf(0);
        if (firstZero != -1){
            pw = pw.substring(0, firstZero);
        }

        byte[] key = {0, 0, 0, 0, 0, 0, 0, 0};
        System.arraycopy(pw.getBytes(), 0, key, 0, pw.length());

        DesCipher des = new DesCipher(key);

        des.encrypt(challenge, 0, challenge, 0);
        des.encrypt(challenge, 8, challenge, 8);

        remoteOutputStream.write(challenge);

        readSecurityResult("VNC authentication");
    }

    public void authenticateARD(String username, String pw) throws Exception {
        Log.d(TAG, "authenticateARD");
        RFBSecurityARD ard = new RFBSecurityARD(username, pw);
        ard.perform(this);
        readSecurityResult("ARD authentication");
    }

    void readSecurityResult(String authType) throws Exception {
        int securityResult = remoteInputStream.readInt();

        switch (securityResult) {
            case VncAuthOK:
                System.out.println(authType + ": success");
                break;
            case VncAuthFailed:
                if (clientMinor >= 8){
                    readConnFailedReason();
                }
                throw new Exception(authType + ": failed");
            case VncAuthTooMany:
                throw new Exception(authType + ": failed, too many tries");
            default:
                throw new Exception(authType + ": unknown result " + securityResult);
        }
    }

    void readConnFailedReason() throws Exception {
        int reasonLen = remoteInputStream.readInt();
        byte[] reason = new byte[reasonLen];
        readFully(reason);

        String reasonString = new String(reason);
        Log.v(TAG, reasonString);
        throw new Exception(reasonString);
    }

    public void prepareDH() throws Exception {
        long gen = remoteInputStream.readLong();
        long mod = remoteInputStream.readLong();
        dh_resp = remoteInputStream.readLong();

        dh = new DH(gen, mod);
        long pub = dh.createInterKey();

        remoteOutputStream.write(DH.longToBytes(pub));
    }

    public void authenticateDH(String us, String pw) throws Exception {
        long key = dh.createEncryptionKey(dh_resp);

        DesCipher des = new DesCipher(DH.longToBytes(key));

        byte user[] = new byte[256];
        byte passwd[] = new byte[64];
        int i;
        System.arraycopy(us.getBytes(), 0, user, 0, us.length());
        if (us.length() < 256) {
            for (i = us.length(); i < 256; i++) {
                user[i] = 0;
            }
        }
        System.arraycopy(pw.getBytes(), 0, passwd, 0, pw.length());
        if (pw.length() < 64) {
            for (i = pw.length(); i < 64; i++) {
                passwd[i] = 0;
            }
        }

        des.encryptText(user, user, DH.longToBytes(key));
        des.encryptText(passwd, passwd, DH.longToBytes(key));

        remoteOutputStream.write(user);
        remoteOutputStream.write(passwd);

        readSecurityResult("VNC authentication");
    }


    //
    // Create session file and write initial protocol messages into it.
    //
  /*-
  void startSession(String fname) throws IOException {
    rec = new SessionRecorder(fname);
    rec.writeHeader();
    rec.write(versionMsg_3_3.getBytes());
    rec.writeIntBE(SecTypeNone);
    rec.writeShortBE(framebufferWidth);
    rec.writeShortBE(framebufferHeight);
    byte[] fbsServerInitMsg =	{
      32, 24, 0, 1, 0,
      (byte)0xFF, 0, (byte)0xFF, 0, (byte)0xFF,
      16, 8, 0, 0, 0, 0
    };
    rec.write(fbsServerInitMsg);
    rec.writeIntBE(desktopName.length());
    rec.write(desktopName.getBytes());
    numUpdatesInSession = 0;

    // FIXME: If there were e.g. ZRLE updates only, that should not
    //        affect recording of Zlib and Tight updates. So, actually
    //        we should maintain separate flags for Zlib, ZRLE and
    //        Tight, instead of one ``wereZlibUpdates'' variable.
    //
    if (wereZlibUpdates)
      recordFromBeginning = false;

    zlibWarningShown = false;
    tightWarningShown = false;
  }

  //
  // Close session file.
  //

  void closeSession() throws IOException {
    if (rec != null) {
      rec.close();
      rec = null;
    }
  }
  */

    //
    // Set new framebuffer size
    //

    public void initCapabilities() {
        tunnelCaps = new CapsContainer();
        authCaps = new CapsContainer();
        serverMsgCaps = new CapsContainer();
        clientMsgCaps = new CapsContainer();
        encodingCaps = new CapsContainer();

        // Supported authentication methods
        authCaps.add(AuthNone, StandardVendor, SigAuthNone,
                "No authentication");
        authCaps.add(AuthVNC, StandardVendor, SigAuthVNC,
                "Standard VNC password authentication");

        // Supported encoding types
        encodingCaps.add(EncodingCopyRect, StandardVendor,
                SigEncodingCopyRect, "Standard CopyRect encoding");
        encodingCaps.add(EncodingRRE, StandardVendor,
                SigEncodingRRE, "Standard RRE encoding");
        encodingCaps.add(EncodingCoRRE, StandardVendor,
                SigEncodingCoRRE, "Standard CoRRE encoding");
        encodingCaps.add(EncodingHextile, StandardVendor,
                SigEncodingHextile, "Standard Hextile encoding");
        encodingCaps.add(EncodingZRLE, StandardVendor,
                SigEncodingZRLE, "Standard ZRLE encoding");
        encodingCaps.add(EncodingZlib, TridiaVncVendor,
                SigEncodingZlib, "Zlib encoding");
        encodingCaps.add(EncodingTight, TightVncVendor,
                SigEncodingTight, "Tight encoding");

        // Supported pseudo-encoding types
        encodingCaps.add(EncodingCompressLevel0, TightVncVendor,
                SigEncodingCompressLevel0, "Compression level");
        encodingCaps.add(EncodingQualityLevel0, TightVncVendor,
                SigEncodingQualityLevel0, "JPEG quality level");
        encodingCaps.add(EncodingXCursor, TightVncVendor,
                SigEncodingXCursor, "X-style cursor shape update");
        encodingCaps.add(EncodingRichCursor, TightVncVendor,
                SigEncodingRichCursor, "Rich-color cursor shape update");
        encodingCaps.add(EncodingPointerPos, TightVncVendor,
                SigEncodingPointerPos, "Pointer position update");
        encodingCaps.add(EncodingLastRect, TightVncVendor,
                SigEncodingLastRect, "LastRect protocol extension");
        encodingCaps.add(EncodingNewFBSize, TightVncVendor,
                SigEncodingNewFBSize, "Framebuffer size change");
    }


    //
    // Read the server message type
    //

    public void setupTunneling() throws IOException {
        int nTunnelTypes = remoteInputStream.readInt();
        if (nTunnelTypes != 0) {
            readCapabilityList(tunnelCaps, nTunnelTypes);

            // We don't support tunneling yet.
            writeInt(NoTunneling);
        }
    }


    //
    // Read a FramebufferUpdate message
    //

    public int negotiateAuthenticationTight() throws Exception {
        int nAuthTypes = remoteInputStream.readInt();
        if (nAuthTypes == 0)
            return AuthNone;

        readCapabilityList(authCaps, nAuthTypes);
        for (int i = 0; i < authCaps.numEnabled(); i++) {
            int authType = authCaps.getByOrder(i);
            if (authType == AuthNone || authType == AuthVNC) {
                writeInt(authType);
                return authType;
            }
        }
        throw new Exception("No suitable authentication scheme found");
    }

    void readCapabilityList(CapsContainer caps, int count) throws IOException {
        int code;
        byte[] vendor = new byte[4];
        byte[] name = new byte[8];
        for (int i = 0; i < count; i++) {
            code = remoteInputStream.readInt();
            readFully(vendor);
            readFully(name);
            caps.enable(new CapabilityInfo(code, vendor, name));
        }
    }

    // Read a FramebufferUpdate rectangle header

    void writeInt(int value) throws IOException {
        writeIntBuffer[0] = (byte) ((value >> 24) & 0xff);
        writeIntBuffer[1] = (byte) ((value >> 16) & 0xff);
        writeIntBuffer[2] = (byte) ((value >> 8) & 0xff);
        writeIntBuffer[3] = (byte) (value & 0xff);
        remoteOutputStream.write(writeIntBuffer);
    }

    public void writeClientInit() throws IOException {
    /*- if (viewer.options.shareDesktop) {
      os.write(1);
    } else {
      os.write(0);
    }
    viewer.options.disableShareDesktop();
    */
        remoteOutputStream.write(0);
    }

    // Read CopyRect source X and Y.

    public void readServerInit() throws IOException {
        framebufferWidth = remoteInputStream.readUnsignedShort();
        framebufferHeight = remoteInputStream.readUnsignedShort();
        bitsPerPixel = remoteInputStream.readUnsignedByte();
        depth = remoteInputStream.readUnsignedByte();
        bigEndian = (remoteInputStream.readUnsignedByte() != 0);
        trueColour = (remoteInputStream.readUnsignedByte() != 0);
        redMax = remoteInputStream.readUnsignedShort();
        greenMax = remoteInputStream.readUnsignedShort();
        blueMax = remoteInputStream.readUnsignedShort();
        redShift = remoteInputStream.readUnsignedByte();
        greenShift = remoteInputStream.readUnsignedByte();
        blueShift = remoteInputStream.readUnsignedByte();
        byte[] pad = new byte[3];
        readFully(pad);
        int nameLength = remoteInputStream.readInt();
        byte[] name = new byte[nameLength];
        readFully(name);
        desktopName = new String(name);

        // Read interaction capabilities (TightVNC protocol extensions)
        if (protocolTightVNC) {
            int nServerMessageTypes = remoteInputStream.readUnsignedShort();
            int nClientMessageTypes = remoteInputStream.readUnsignedShort();
            int nEncodingTypes = remoteInputStream.readUnsignedShort();
            remoteInputStream.readUnsignedShort();
            readCapabilityList(serverMsgCaps, nServerMessageTypes);
            readCapabilityList(clientMsgCaps, nClientMessageTypes);
            readCapabilityList(encodingCaps, nEncodingTypes);
        }

        inNormalProtocol = true;
    }

    public void setFramebufferSize(int width, int height) {
        framebufferWidth = width;
        framebufferHeight = height;
    }


    //
    // Read a ServerCutText message
    //

    public int readServerMessageType() throws IOException {
        int msgType = remoteInputStream.readUnsignedByte();

        // If the session remoteInputStream being recorded:
    /*-
    if (rec != null) {
      if (msgType == Bell) {	// Save Bell messages in session files.
	rec.writeByte(msgType);
	if (numUpdatesInSession > 0)
	  rec.flush();
      }
    }
	*/

        return msgType;
    }


    //
    // Read an integer in compact representation (1..3 bytes).
    // Such format remoteInputStream used as a part of the Tight encoding.
    // Also, this method records data if session recording remoteInputStream active and
    // the viewer's recordFromBeginning variable remoteInputStream set to true.
    //

    public void readFramebufferUpdate() throws IOException {
        remoteInputStream.readByte();
        updateNRects = remoteInputStream.readUnsignedShort();

        // If the session remoteInputStream being recorded:
    /*-
    if (rec != null) {
      rec.writeByte(FramebufferUpdate);
      rec.writeByte(0);
      rec.writeShortBE(updateNRects);
    }
	*/

        numUpdatesInSession++;
    }


    //
    // Write a FramebufferUpdateRequest message
    //

    public void readFramebufferUpdateRectHdr() throws Exception {
        updateRectX = remoteInputStream.readUnsignedShort();
        updateRectY = remoteInputStream.readUnsignedShort();
        updateRectW = remoteInputStream.readUnsignedShort();
        updateRectH = remoteInputStream.readUnsignedShort();
        updateRectEncoding = remoteInputStream.readInt();

        if (updateRectEncoding == EncodingZlib ||
                updateRectEncoding == EncodingZRLE ||
                updateRectEncoding == EncodingTight)
            wereZlibUpdates = true;

        // If the session remoteInputStream being recorded:
    /*-
    if (rec != null) {
      if (numUpdatesInSession > 1)
	rec.flush();		// Flush the output on each rectangle.
      rec.writeShortBE(updateRectX);
      rec.writeShortBE(updateRectY);
      rec.writeShortBE(updateRectW);
      rec.writeShortBE(updateRectH);
      if (updateRectEncoding == EncodingZlib && !recordFromBeginning) {
	// Here we cannot write Zlib-encoded rectangles because the
	// decoder won't be able to reproduce zlib stream state.
	if (!zlibWarningShown) {
	  System.out.println("Warning: Raw encoding will be used " +
			     "instead of Zlib in recorded session.");
	  zlibWarningShown = true;
	}
	rec.writeIntBE(EncodingRaw);
      } else {
	rec.writeIntBE(updateRectEncoding);
	if (updateRectEncoding == EncodingTight && !recordFromBeginning &&
	    !tightWarningShown) {
	  System.out.println("Warning: Re-compressing Tight-encoded " +
			     "updates for session recording.");
	  tightWarningShown = true;
	}
      }
    }
	*/

        if (updateRectEncoding != RfbProto.EncodingPointerPos && (updateRectEncoding < 0 || updateRectEncoding > MaxNormalEncoding))
            return;

        if (updateRectX + updateRectW > framebufferWidth ||
                updateRectY + updateRectH > framebufferHeight) {
            throw new Exception("Framebuffer update rectangle too large: " +
                    updateRectW + "x" + updateRectH + " at (" +
                    updateRectX + "," + updateRectY + ")");
        }
    }

    public void readCopyRect() throws IOException {
        copyRectSrcX = remoteInputStream.readUnsignedShort();
        copyRectSrcY = remoteInputStream.readUnsignedShort();

        // If the session remoteInputStream being recorded:
    /*-
    if (rec != null) {
      rec.writeShortBE(copyRectSrcX);
      rec.writeShortBE(copyRectSrcY);
    }
    */
    }


    //
    // Write a SetPixelFormat message
    //

    public String readServerCutText() throws IOException {
        byte[] pad = new byte[3];
        readFully(pad);
        int len = remoteInputStream.readInt();
        byte[] text = new byte[len];
        readFully(text);
        return new String(text);
    }


    //
    // Write a FixColourMapEntries message.  The values in the red, green and
    // blue arrays are from 0 to 65535.
    //

    int readCompactLen() throws IOException {
        int[] portion = new int[3];
        portion[0] = remoteInputStream.readUnsignedByte();
        int byteCount = 1;
        int len = portion[0] & 0x7F;
        if ((portion[0] & 0x80) != 0) {
            portion[1] = remoteInputStream.readUnsignedByte();
            byteCount++;
            len |= (portion[1] & 0x7F) << 7;
            if ((portion[1] & 0x80) != 0) {
                portion[2] = remoteInputStream.readUnsignedByte();
                byteCount++;
                len |= (portion[2] & 0xFF) << 14;
            }
        }
    /*-
    if (rec != null && recordFromBeginning)
      for (int i = 0; i < byteCount; i++)
	rec.writeByte(portion[i]);
	*/
        return len;
    }


    //
    // Write a SetEncodings message
    //

    public synchronized void writeFramebufferUpdateRequest(int x, int y, int w, int h,
                                                           boolean incremental)
            throws IOException {
        framebufferUpdateRequest[0] = (byte) FramebufferUpdateRequest;
        framebufferUpdateRequest[1] = (byte) (incremental ? 1 : 0);
        framebufferUpdateRequest[2] = (byte) ((x >> 8) & 0xff);
        framebufferUpdateRequest[3] = (byte) (x & 0xff);
        framebufferUpdateRequest[4] = (byte) ((y >> 8) & 0xff);
        framebufferUpdateRequest[5] = (byte) (y & 0xff);
        framebufferUpdateRequest[6] = (byte) ((w >> 8) & 0xff);
        framebufferUpdateRequest[7] = (byte) (w & 0xff);
        framebufferUpdateRequest[8] = (byte) ((h >> 8) & 0xff);
        framebufferUpdateRequest[9] = (byte) (h & 0xff);

        remoteOutputStream.write(framebufferUpdateRequest);
    }


    //
    // Write a ClientCutText message
    //

    public synchronized void writeSetPixelFormat(int bitsPerPixel, int depth, boolean bigEndian,
                                                 boolean trueColour,
                                                 int redMax, int greenMax, int blueMax,
                                                 int redShift, int greenShift, int blueShift, boolean fGreyScale) // sf@2005)
            throws IOException {
        byte[] b = new byte[20];

        b[0] = (byte) SetPixelFormat;
        b[4] = (byte) bitsPerPixel;
        b[5] = (byte) depth;
        b[6] = (byte) (bigEndian ? 1 : 0);
        b[7] = (byte) (trueColour ? 1 : 0);
        b[8] = (byte) ((redMax >> 8) & 0xff);
        b[9] = (byte) (redMax & 0xff);
        b[10] = (byte) ((greenMax >> 8) & 0xff);
        b[11] = (byte) (greenMax & 0xff);
        b[12] = (byte) ((blueMax >> 8) & 0xff);
        b[13] = (byte) (blueMax & 0xff);
        b[14] = (byte) redShift;
        b[15] = (byte) greenShift;
        b[16] = (byte) blueShift;
        b[17] = (byte) (fGreyScale ? 1 : 0); // sf@2005

        remoteOutputStream.write(b);
    }


    //
    // A buffer for putting pointer and keyboard events before being sent.  This
    // remoteInputStream to ensure that multiple RFB events generated from a single Java Event
    // will all be sent in a single network packet.  The maximum possible
    // length remoteInputStream 4 modifier down events, a single key event followed by 4
    // modifier up events i.e. 9 key events or 72 bytes.
    //

    synchronized void writeFixColourMapEntries(int firstColour, int nColours,
                                               int[] red, int[] green, int[] blue)
            throws IOException {
        byte[] b = new byte[6 + nColours * 6];

        b[0] = (byte) FixColourMapEntries;
        b[2] = (byte) ((firstColour >> 8) & 0xff);
        b[3] = (byte) (firstColour & 0xff);
        b[4] = (byte) ((nColours >> 8) & 0xff);
        b[5] = (byte) (nColours & 0xff);

        for (int i = 0; i < nColours; i++) {
            b[6 + i * 6] = (byte) ((red[i] >> 8) & 0xff);
            b[6 + i * 6 + 1] = (byte) (red[i] & 0xff);
            b[6 + i * 6 + 2] = (byte) ((green[i] >> 8) & 0xff);
            b[6 + i * 6 + 3] = (byte) (green[i] & 0xff);
            b[6 + i * 6 + 4] = (byte) ((blue[i] >> 8) & 0xff);
            b[6 + i * 6 + 5] = (byte) (blue[i] & 0xff);
        }

        remoteOutputStream.write(b);
    }

    public synchronized void writeSetEncodings(int[] encs, int len) throws IOException {
        byte[] b = new byte[4 + 4 * len];

        b[0] = (byte) SetEncodings;
        b[2] = (byte) ((len >> 8) & 0xff);
        b[3] = (byte) (len & 0xff);

        for (int i = 0; i < len; i++) {
            b[4 + 4 * i] = (byte) ((encs[i] >> 24) & 0xff);
            b[5 + 4 * i] = (byte) ((encs[i] >> 16) & 0xff);
            b[6 + 4 * i] = (byte) ((encs[i] >> 8) & 0xff);
            b[7 + 4 * i] = (byte) (encs[i] & 0xff);
        }

        remoteOutputStream.write(b);
    }

    public synchronized void writeClientCutText(String text) throws IOException {
        byte[] b = new byte[8 + text.length()];

        b[0] = (byte) ClientCutText;
        b[4] = (byte) ((text.length() >> 24) & 0xff);
        b[5] = (byte) ((text.length() >> 16) & 0xff);
        b[6] = (byte) ((text.length() >> 8) & 0xff);
        b[7] = (byte) (text.length() & 0xff);

        System.arraycopy(text.getBytes(), 0, b, 8, text.length());

        remoteOutputStream.write(b);
    }

    /**
     * Write a pointer event message.  We may need to send modifier key events
     * around it to set the correct modifier state.
     *
     * @param x
     * @param y
     * @param modifiers
     * @param pointerMask
     * @throws IOException
     */
    public synchronized void writePointerEvent(int x, int y, int modifiers, int pointerMask) throws IOException {
        if (Utils.DEBUG()){
            Log.d(TAG, "Input: writerPointerEvent: x:" + x + " y:" + y + " pointerMask:" + pointerMask);
        }

        eventBufLen = 0;
        writeModifierKeyEvents(modifiers);

        eventBuf[eventBufLen++] = (byte) PointerEvent;
        eventBuf[eventBufLen++] = (byte) pointerMask;
        eventBuf[eventBufLen++] = (byte) ((x >> 8) & 0xff);
        eventBuf[eventBufLen++] = (byte) (x & 0xff);
        eventBuf[eventBufLen++] = (byte) ((y >> 8) & 0xff);
        eventBuf[eventBufLen++] = (byte) (y & 0xff);

        //
        // Always release all modifiers after an "up" event
        //
        if (pointerMask == 0) {
            writeModifierKeyEvents(0);
        }

        remoteOutputStream.write(eventBuf, 0, eventBufLen);
    }

    void writeCtrlAltDel() throws IOException {
        final int DELETE = 0xffff;
        final int CTRLALT = VNCConn.CTRL_MASK | VNCConn.ALT_MASK;
        try {
            // Press
            eventBufLen = 0;
            writeModifierKeyEvents(CTRLALT);
            writeKeyEvent(DELETE, true);
            remoteOutputStream.write(eventBuf, 0, eventBufLen);

            // Release
            eventBufLen = 0;
            writeModifierKeyEvents(CTRLALT);
            writeKeyEvent(DELETE, false);

            // Reset VNC server modifiers state
            writeModifierKeyEvents(0);
            remoteOutputStream.write(eventBuf, 0, eventBufLen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //
    // Add a raw key event with the given X keysym to eventBuf.
    //

    //
    // Write a key event message.  We may need to send modifier key events
    // around it to set the correct modifier state.  Also we need to translate
    // from the Java key values to the X keysym values used by the RFB protocol.
    //
    public synchronized void writeKeyEvent(int keySym, int metaState, boolean down) throws IOException {
        eventBufLen = 0;
        if (down){
            writeModifierKeyEvents(metaState);
        }
        if (keySym != 0){
            writeKeyEvent(keySym, down);
        }

        // Always release all modifiers after an "up" event
        if (!down){
            writeModifierKeyEvents(0);
        }

        remoteOutputStream.write(eventBuf, 0, eventBufLen);
    }


    //
    // Write key events to set the correct modifier state.
    //

    private void writeKeyEvent(int keysym, boolean down) {
        eventBuf[eventBufLen++] = (byte) KeyboardEvent;
        eventBuf[eventBufLen++] = (byte) (down ? 1 : 0);
        eventBuf[eventBufLen++] = (byte) 0;
        eventBuf[eventBufLen++] = (byte) 0;
        eventBuf[eventBufLen++] = (byte) ((keysym >> 24) & 0xff);
        eventBuf[eventBufLen++] = (byte) ((keysym >> 16) & 0xff);
        eventBuf[eventBufLen++] = (byte) ((keysym >> 8) & 0xff);
        eventBuf[eventBufLen++] = (byte) (keysym & 0xff);
    }

    void writeModifierKeyEvents(int newModifiers) {
        if ((newModifiers & VNCConn.CTRL_MASK) != (oldModifiers & VNCConn.CTRL_MASK)){
            writeKeyEvent(0xffe3, (newModifiers & VNCConn.CTRL_MASK) != 0);
        }

        if ((newModifiers & VNCConn.SHIFT_MASK) != (oldModifiers & VNCConn.SHIFT_MASK)){
            writeKeyEvent(0xffe1, (newModifiers & VNCConn.SHIFT_MASK) != 0);
        }

        if ((newModifiers & VNCConn.META_MASK) != (oldModifiers & VNCConn.META_MASK)){
            writeKeyEvent(0xffe7, (newModifiers & VNCConn.META_MASK) != 0);
        }

        if ((newModifiers & VNCConn.ALT_MASK) != (oldModifiers & VNCConn.ALT_MASK)){
            writeKeyEvent(0xffe9, (newModifiers & VNCConn.ALT_MASK) != 0);
        }

        oldModifiers = newModifiers;
    }
    //
    // Compress and write the data into the recorded session file. This
    // method assumes the recording remoteInputStream on (rec != null).
    //


    public void startTiming() {
        timing = true;

        // Carry over up to 1s worth of previous rate for smoothing.

        if (timeWaitedIn100us > 10000) {
            timedKbits = timedKbits * 10000 / timeWaitedIn100us;
            timeWaitedIn100us = 10000;
        }
    }

    public void stopTiming() {
        timing = false;
        if (timeWaitedIn100us < timedKbits / 2){
            timeWaitedIn100us = timedKbits / 2; // upper limit 20Mbit/s
        }
    }

    public long kbitsPerSecond() {
        return timedKbits * 10000 / timeWaitedIn100us;
    }

    public long timeWaited() {
        return timeWaitedIn100us;
    }

    public void readFully(byte b[]) throws IOException {
        readFully(b, 0, b.length);
    }

    public void readFully(byte b[], int off, int len) throws IOException {
        long before = 0;
        timing = false; // for test

        if (timing){
            before = System.currentTimeMillis();
        }

        remoteInputStream.readFully(b, off, len);

        if (timing) {
            long after = System.currentTimeMillis();
            long newTimeWaited = (after - before) * 10;
            int newKbits = len * 8 / 1000;

            // limit rate to between 10kbit/s and 40Mbit/s

            if (newTimeWaited > newKbits * 1000) newTimeWaited = newKbits * 1000;
            if (newTimeWaited < newKbits / 4) newTimeWaited = newKbits / 4;

            timeWaitedIn100us += newTimeWaited;
            timedKbits += newKbits;
        }
    }

    synchronized void writeOpenChat() throws Exception {
        remoteOutputStream.write(TextChat); // byte type
        remoteOutputStream.write(0); // byte pad 1
        remoteOutputStream.write(0); // byte pad 2
        remoteOutputStream.write(0); // byte pad 2
        writeInt(CHAT_OPEN); // int message length
    }

    synchronized void writeCloseChat() throws Exception {
        remoteOutputStream.write(TextChat); // byte type
        remoteOutputStream.write(0); // byte pad 1
        remoteOutputStream.write(0); // byte pad 2
        remoteOutputStream.write(0); // byte pad 2
        writeInt(CHAT_CLOSE); // int message length
    }

    synchronized void writeFinishedChat() throws Exception {
        remoteOutputStream.write(TextChat); // byte type
        remoteOutputStream.write(0); // byte pad 1
        remoteOutputStream.write(0); // byte pad 2
        remoteOutputStream.write(0); // byte pad 2
        writeInt(CHAT_FINISHED); // int message length
    }

    public String readTextChatMsg() throws Exception {
        byte[] pad = new byte[3];
        readFully(pad);
        int len = remoteInputStream.readInt();
        if (len == CHAT_OPEN) {
            // Remote user requests chat
            ///viewer.openChat();
            // Respond to chat request
            writeOpenChat();
            return null;
        } else if (len == CHAT_CLOSE) {
            // Remote user ends chat
            ///viewer.closeChat();
            return null;
        } else if (len == CHAT_FINISHED) {
            // Remote user says chat finished.
            // Not sure why I should care about this state.
            return null;
        } else {
            // Remote user sends message!!
            if (len > 0) {
                byte[] msg = new byte[len];
                readFully(msg);
                return new String(msg);
            }
        }
        return null;
    }

    public synchronized void writeChatMessage(String msg) throws Exception {
        remoteOutputStream.write(TextChat); // byte type
        remoteOutputStream.write(0); // byte pad 1
        remoteOutputStream.write(0); // byte pad 2
        remoteOutputStream.write(0); // byte pad 2
        byte[] bytes = msg.getBytes("8859_1");
        byte[] outgoing = bytes;
        if (bytes.length > 4096) {
            outgoing = new byte[4096];
            System.arraycopy(bytes, 0, outgoing, 0, 4096);
        }
        writeInt(outgoing.length); // int message length
        remoteOutputStream.write(outgoing); // message
    }
}
