package phpcs;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.msg.BufferUpdate;
import org.gjt.sp.jedit.EBMessage;

import errorlist.ErrorSource;
import errorlist.DefaultErrorSource;

public class PHPCSPlugin extends EBPlugin {
    private static DefaultErrorSource errorSource;
    private static PHPCSPlugin me;

    public void start() {
        errorSource = new DefaultErrorSource("phpcs");
        ErrorSource.registerErrorSource(errorSource);
        me = this;
    }

    public void handleMessage(EBMessage ebmess) {
        if (ebmess instanceof BufferUpdate && jEdit.getBooleanProperty("options.phpcs.runonsave")) {
            BufferUpdate bu = (BufferUpdate)ebmess;
            if (bu.getWhat() == BufferUpdate.SAVED) {
                checkBuffer(bu.getView());
            }
        }
    }

    public static void checkBuffer(View v) {
        errorSource.clear();
        me.checkFile(v.getBuffer().getPath());
    }

    public static void checkBuffers(View v) {
        errorSource.clear();
        Buffer[] buffers = jEdit.getBuffers();
        for (int i = 0; i < buffers.length; i++) {
            Buffer buffer = buffers[i];
            me.checkFile(buffer.getPath());
        }
    }

    public static void checkFile(String filename) {
        String phpcsPath     = jEdit.getProperty("options.phpcs.path");
        String phpcsStandard = jEdit.getProperty("options.phpcs.standard");
        String command       = phpcsPath + " --standard=" + phpcsStandard + " --report=checkstyle " + filename;

        try {
            String xmlOutput = "";
            String outLine   = null;
            Process process  = Runtime.getRuntime().exec(command);
            BufferedReader breader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while((outLine = breader.readLine()) != null) {
                xmlOutput += outLine;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inStream = new InputSource();
            inStream.setCharacterStream(new StringReader(xmlOutput));
            Document doc = db.parse(inStream);

            NodeList errors = doc.getElementsByTagName("error");
            for (int s = 0; s < errors.getLength() ; s++) {
                Element error = (Element)errors.item(s);
                int line    = Integer.parseInt(error.getAttribute("line"));
                int col     = Integer.parseInt(error.getAttribute("column"));
                String msg  = error.getAttribute("message");
                String src  = error.getAttribute("source");

                String severity = error.getAttribute("severity");
                int errSev = ErrorSource.ERROR;
                if (severity.equals("warning")) {
                    errSev = ErrorSource.WARNING;
                }

                errorSource.addError(
                    errSev, filename,
                    (line - 1), (col - 1), col,
                    msg + " (" + src +")"
                );
            }

        } catch(Exception e) {
        }
    }

    public void stop() {
        ErrorSource.unregisterErrorSource(errorSource);
        errorSource = null;
        me = null;
    }
}
