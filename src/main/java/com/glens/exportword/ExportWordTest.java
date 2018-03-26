package com.glens.exportword;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lowagie.text.Image;

/**
 * @ClassName: ExportWordTest.java
 * @Description: TODO(导出Word)
 * 
 * @author GaoJiaNan
 * @version V1.0
 * @Date 2015-5-25 下午12:12:35
 */
@Controller
@RequestMapping("exportWordTest.do")
public class ExportWordTest {
	@RequestMapping(method = RequestMethod.POST, params = "method=exportWordDoc")
	public void exportWordDoc(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String result = "";

		response.setHeader("content-disposition", "attachment;filename="
				+ new SimpleDateFormat("yyyyMMddHH:mm:ss").format(new Date())
				+ ".docx");

		String svgCode = request.getParameter("svg");// highcharts图表svgCode
		String svg[] = svgCode.split("_");
		String path[] = new String[svg.length];

		List<String> imageList = new ArrayList<String>();

		String dowPatch = request.getSession().getServletContext()
				.getRealPath("/")
				+ "/file/cache/";

		if (svg != null) {
			File dowFile = new File(dowPatch);

			if (!dowFile.exists() && !dowFile.isDirectory()) {

				dowFile.mkdirs();
			}

			for (int k = 0; k < svg.length; k++) {
				String picName = new SimpleDateFormat("yyyyMMddHHmmssSSS")
						.format(new Date()) + ".png";
				path[k] = dowPatch + picName;

				imageList.add(path[k]);
				SvgPngConverter.convertToPng(svg[k], path[k]);
			}
		}

		if (imageList.size() > 1) {
			String filePath = request.getSession().getServletContext()
					.getRealPath("/")
					+ "templateDownload/test.docx";

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("${doc_number}", "cg20150528001");

			List<String> chartStr = new ArrayList<String>();
			chartStr.add("${line}");
			chartStr.add("${pic}");
			Map<String, Object> chartMap = null;
			Image img = null;
			for (int j = 0; j < imageList.size(); j++) {
				// 图片
				img = Image.getInstance(imageList.get(j));
				float heigth = img.getHeight();
				float width = img.getWidth();

				chartMap = new HashMap<String, Object>();
				chartMap.put("width", width);
				chartMap.put("height", heigth);
				chartMap.put("type", "jpg");
				chartMap.put("content", WordUtil.inputStream2ByteArray(
						new FileInputStream(imageList.get(j)), true));
				param.put(chartStr.get(j), chartMap);
			}

			List<Map<String, List<String>>> tabListMaps = new ArrayList<Map<String, List<String>>>();
			Map<String, List<String>> tabMaps = null;
			List<String> tabList = null;
			for (int i = 0; i < 10; i++) {
				tabList = new ArrayList<String>();
				tabList.add("索尼相机");
				tabList.add("金鹰索尼" + i + "号专柜");
				tabList.add("8695" + i);
				tabMaps = new HashMap<String, List<String>>();
				tabMaps.put("${tb1}", tabList);
				tabListMaps.add(tabMaps);
			}
			
			for (int i = 0; i < 5; i++) {
				tabList = new ArrayList<String>();
				tabList.add("尼康相机");
				tabList.add("金鹰尼康" + i + "号专柜");
				tabList.add("8695" + i);
				tabMaps = new HashMap<String, List<String>>();
				tabMaps.put("${tb2}", tabList);
				tabListMaps.add(tabMaps);
			}

			CustomXWPFDocument doc = WordUtil.generateWord(param, filePath,
					tabListMaps);
			FileOutputStream fopts = new FileOutputStream(dowPatch
					+ "test.docx");
			doc.write(fopts);
			fopts.close();

			result = "/file/cache/test.docx";
			result = result.replace("\\", "/").substring(1);
		} else {
			result = "不存在附件";
		}

		PrintWriter writer;
		try {
			writer = response.getWriter();

			writer.write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
