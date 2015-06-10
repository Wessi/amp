package org.dgfoundation.amp.reports.saiku.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.saiku.web.export.PdfReport;
import org.saiku.web.rest.objects.resultset.QueryResult;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class AMPPdfExport extends PdfReport {

	public byte[] pdf(QueryResult qr, String svg) throws Exception {
		return super.pdf(qr, svg);
	}

	public byte[] pdf(JsonBean qr, String svg) throws Exception {
		Rectangle size = PageSize.A4.rotate();
		Document doc = new Document(size);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(doc, os);
		doc.open();
		populatePdf(doc, writer, qr);
		doc.close();
		return os.toByteArray();
	}

	public void populatePdf(Document doc, PdfWriter writer, JsonBean jb) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		String content = AMPJSConverter.convertToHtml(jb);
		content = "<div>" + "AMP Export - " + dateFormat.format(date) + "</div><div>&nbsp;</div>" + content;

		InputStream contentIs = new ByteArrayInputStream(content.getBytes("UTF-8"));
		// CSS
		CSSResolver cssResolver = new StyleAttrCSSResolver();
		CssFile cssFile = XMLWorkerHelper.getCSS(getClass().getResourceAsStream("saiku.table.pdf.css"));
		cssResolver.addCss(cssFile);
		// HTML
		XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(this.getClass().getResource("").getPath());
		fontProvider.defaultEncoding = "UTF-8";
		CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		// Pipelines
		PdfWriterPipeline pdf = new PdfWriterPipeline(doc, writer);
		HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
		CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
		XMLWorker worker = new XMLWorker(css, true);
		XMLParser p = new XMLParser(worker);
		p.parse(contentIs, true);
		int n = contentIs.available();
		byte[] bytes = new byte[n];
		contentIs.read(bytes, 0, n);
	}
}
