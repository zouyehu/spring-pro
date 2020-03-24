package com.zyh.hu.utils;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

import com.zyh.hu.service.CollectionWrapper;

@SuppressWarnings({"unchecked", "rawtypes"})
public class JaxbUtils {

	private JAXBContext jaxbContext;

	private Logger logger = LoggerFactory.getLogger(JaxbUtils.class);

	/**
	 * 需要序列化的root对象的类型
	 * 
	 * @param types
	 */
	public JaxbUtils(Class<?>... types) {
		try {
			jaxbContext = JAXBContext.newInstance(types);
		} catch (JAXBException e) {
			logger.error("---异常信息:" + e);
		}
	}

	/**
	 * 
	 * @param root
	 * @param encoding
	 * @return
	 */
	public String toXml(Object root, String encoding) {
		try {
			StringWriter out = new StringWriter();
			OutputFormat format = new OutputFormat();
			format.setIndent(true);
			format.setNewlines(true);
			format.setNewLineAfterDeclaration(false);
			XMLWriter writer = new XMLWriter(out, format);
			XMLFilterImpl nsfFilter = new XMLFilterImpl() {
				private boolean ignoreNamespace = false;
				private String rootNamespace = null;
				private boolean isRootElement = true;

				@Override
				public void startDocument() throws SAXException {
					super.startDocument();
				}

				@Override
				public void startElement(String uri, String localName,
						String qName, Attributes atts) throws SAXException {
					if (this.ignoreNamespace)
						uri = "";
					if (this.isRootElement)
						this.isRootElement = false;
					else if (!uri.equals("") && !localName.contains("xmlns"))
						localName = localName + " xmlns=\"" + uri + "\"";

					super.startElement(uri, localName, localName, atts);
				}

				@Override
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					if (this.ignoreNamespace)
						uri = "";
					super.endElement(uri, localName, localName);
				}

				@Override
				public void startPrefixMapping(String prefix, String url)
						throws SAXException {
					if (this.rootNamespace != null)
						url = this.rootNamespace;
					if (!this.ignoreNamespace)
						super.startPrefixMapping("", url);

				}
			};
			nsfFilter.setContentHandler(writer);
			createMarshaller(encoding).marshal(root, nsfFilter);
			return out.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	/**
	 * 特别支持对root Element是Collection的情形
	 */
	public String toXml(Collection root, String rootName, String encoding) {
		try {
			CollectionWrapper wrapper = new CollectionWrapper();
			wrapper.collection = root;
			JAXBElement<CollectionWrapper> wrapperElement = new JAXBElement<CollectionWrapper>(
					new QName(rootName), CollectionWrapper.class, wrapper);
			StringWriter writer = new StringWriter();
			createMarshaller(encoding).marshal(wrapperElement, writer);
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public <T> T fromXml(String xml) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnMarshaller().unmarshal(reader);

		} catch (Exception e) {
			throw new RuntimeException(e);

		}
	}

	public <T> T fromXml(String xml, boolean caseSensitive) {
		try {

			String fromXml = xml;
			if (!caseSensitive)
				fromXml = xml.toLowerCase();
			StringReader reader = new StringReader(fromXml);
			return (T) createUnMarshaller().unmarshal(reader);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 创建 MARSHALLER,设定encoding（可为null）
	 * 
	 * @param encoding
	 * @return
	 */
	public Marshaller createMarshaller(String encoding) {
		try {
			Marshaller marshaller = jaxbContext.createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);

			if (!"".equals(encoding)) {
				marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			}
			return marshaller;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Unmarshaller createUnMarshaller() {
		try {
			return jaxbContext.createUnmarshaller();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
