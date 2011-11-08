package org.fit.cssbox.swingbox.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.swing.text.AttributeSet;

import org.fit.cssbox.css.NormalOutput;
import org.fit.cssbox.layout.Box;
import org.fit.cssbox.layout.ElementBox;
import org.fit.cssbox.swingbox.SwingBoxDocument;
import org.fit.cssbox.swingbox.SwingBoxDocument.DelegateElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Tries to convert current content to  a text representation.
 *
 * @author Peter Bielik
 * @version 1.0
 * @since 1.0 - 29.1.2011
 */
public class ContentWriter {
    private StringBuilder buffer;

    /**
     * Instantiates a new content writer.
     */
    public ContentWriter() {
	buffer = new StringBuilder(8 * 1024);
    }

    //hint: in the future, implement dumping, of REAL, CURRENT state (in the future, the content will be dynamic)
    //this implementation is just "work-around", because of problems with document :
    //    public StringBuilder write(SwingBoxDocument doc, int pos, int len) {
    //	Object o = doc.getDefaultRootElement();
    //	DelegateElement de;
    //	//we have custom root-element, so this should be a DelegateElement
    //	if (o instanceof DelegateElement) {
    //	    //DelegateElement has only 1 child, and it should be
    //	    //element, which represents Viewport (CSSBox), so let's get it.
    //	    de = (DelegateElement)o;
    //	    AttributeSet attr = de.getElement(0).getAttributes();
    //	    //in attributes, there should be a box reference (if element is part of SwingBox)
    //	    o = attr.getAttribute(Constants.ATTRIBUTE_BOX_REFERENCE);
    //	    if (o instanceof Box) {
    //		//each box remembers a node, bingo !
    //		Box box = (Box)o;
    //		Node node = box.getNode();
    //		NormalOutput out = new NormalOutput(node);
    //
    //
    //		//System.err.println("@ box: " + box + " node length: " + box.getNode().getChildNodes().getLength());
    //		//hint: this should be reimplemented !!! > use Writer !
    //		ByteArrayOutputStream baos = new ByteArrayOutputStream(8 * 1024);
    //		out.dumpTo(new PrintStream(baos));
    //		try {
    //		    buffer.append(baos.toString("UTF-8"));
    //		} catch (UnsupportedEncodingException e) {
    //		     e.printStackTrace();
    //		}
    //	    }
    //	}
    //	return buffer;
    //    }

    /**
     * Writes current representation of document. If empty output is produced,
     * Document may be processed in parallel (and probably has not finished yet),
     * therefore check if environment variable Constants.DOCUMENT_ASYNCHRONOUS_LOAD_PRIORITY_PROPERTY
     * is set (value >= 0). If so, wait until document is fully processed.
     *
     * @param doc
     *            the W3C Document
     * @return the string builder with textual representation.
     *
     * @see Document
     * @see StringBuilder
     * @see Constants
     * @see GeneralEventListener
     * @see GeneralEvent
     */
    public StringBuilder write(Document doc) {
	if (doc == null) return  buffer;
	NormalOutput out = new NormalOutput(doc.getDocumentElement());


	//hint: this should be reimplemented !!! > use Writer in NormalOutput !
	ByteArrayOutputStream baos = new ByteArrayOutputStream(8 * 1024);
	out.dumpTo(new PrintStream(baos));
	try {
	    buffer.append(baos.toString(Charset.defaultCharset().name()));
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}

	return buffer;
    }

}
