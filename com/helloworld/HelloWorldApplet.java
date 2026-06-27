package com.helloworld;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.Util;

/**
 * HelloWorldApplet
 *
 * Minimal JavaCard applet that returns the ASCII string "Hello World"
 * (hex: 48 65 6C 6C 6F 20 57 6F 72 6C 64) in response to a dedicated APDU.
 *
 * CLA = 0x80, INS = 0x40 -> "Hello World" + SW 0x9000
 */
public class HelloWorldApplet extends Applet {

    private static final byte CLA_HELLO = (byte) 0x80;
    private static final byte INS_HELLO = (byte) 0x40;

    // "Hello World" encoded as raw bytes. Stored once in persistent memory
    // at install time and reused for every response (no per-call allocation).
    private static final byte[] HELLO_WORLD = {
        (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, // "Hello"
        (byte) 0x20,                                                     // ' '
        (byte) 0x57, (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64  // "World"
    };

    private HelloWorldApplet() {
        // register() bez argumentow uzywa AID z sekcji Applet.cap (= AID z -applet w converterze).
        // Jest to wzorzec z oficjalnego przykladu JCDK 2.2.1 HelloWorld od Sun.
        register();
    }

    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new HelloWorldApplet();
    }

    public void process(APDU apdu) {
        // The JCRE calls process() for the SELECT that activates this applet.
        // Returning immediately reports SW 0x9000 for a successful selection.
        if (selectingApplet()) {
            return;
        }

        byte[] buffer = apdu.getBuffer();

        if (buffer[ISO7816.OFFSET_CLA] != CLA_HELLO) {
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        }

        switch (buffer[ISO7816.OFFSET_INS]) {
            case INS_HELLO:
                sendHelloWorld(apdu);
                break;
            default:
                ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
        }
    }

    private void sendHelloWorld(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short length = (short) HELLO_WORLD.length;

        // Copy the constant into the shared APDU buffer (offset 0) before
        // sending. arrayCopyNonAtomic avoids transaction overhead for
        // transient outgoing data.
        Util.arrayCopyNonAtomic(HELLO_WORLD, (short) 0, buffer, (short) 0, length);

        // Declares the outgoing length and transmits the response; SW 0x9000
        // is appended automatically by the JCRE on normal return.
        apdu.setOutgoingAndSend((short) 0, length);
    }
}