package com.goods.admin.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;
import com.goods.book.entity.Book;
import com.goods.book.service.BookService;
import com.goods.category.entity.Category;
import com.goods.category.service.CategoryService;


public class AdminAddBookServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		/*
		 *  commons-fileupload���ϴ�����
		 */
		// ��������
		FileItemFactory factory = new DiskFileItemFactory();
		/*
		 *  ��������������
		 */
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(80 * 1024);//���õ����ϴ����ļ�����Ϊ80KB
		/*
		 *  ����request�õ�List<FileItem>
		 */
		List<FileItem> fileItemList = null;
		try {
			fileItemList = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			// �����������첽��˵�������ļ�������80KB
			error("�ϴ����ļ�������80KB", request, response);
			return;
		}
		
		/*
		 *  ��List<FileItem>��װ��Book������
		 *  ���Ȱѡ���ͨ�����ֶΡ��ŵ�һ��Map�У��ٰ�Mapת����Book��Category�����ٽ������ߵĹ�ϵ
		 */
		Map<String,Object> map = new HashMap<String,Object>();
		for(FileItem fileItem : fileItemList) {
			if(fileItem.isFormField()) {//�������ͨ�����ֶ�
				map.put(fileItem.getFieldName(), fileItem.getString("UTF-8"));
			}
		}
		Book book = CommonUtils.toBean(map, Book.class);//��Map�д󲿷����ݷ�װ��Book������
		Category category = CommonUtils.toBean(map, Category.class);//��Map��cid��װ��Category��
		book.setCategory(category);
		
		/*
		 *  ���ϴ���ͼƬ��������
		 *   > ��ȡ�ļ�������ȡ֮
		 *   > ���ļ�����ǰ׺��ʹ��uuidǰ׺��ΪҲ�����ļ�ͬ������
		 *   > У���ļ�����չ����ֻ����jpg
		 *   > У��ͼƬ�ĳߴ�
		 *   > ָ��ͼƬ�ı���·��������Ҫʹ��ServletContext#getRealPath()
		 *   > ����֮
		 *   > ��ͼƬ��·�����ø�Book����
		 */
		// ��ȡ�ļ���
		FileItem fileItem = fileItemList.get(1);//��ȡ��ͼ
		String filename = fileItem.getName();
		// ��ȡ�ļ�������Ϊ����������ϴ��ľ���·��
		int index = filename.lastIndexOf("\\");
		if(index != -1) {
			filename = filename.substring(index + 1);
		}
		// ���ļ�������uuidǰ׺�������ļ�ͬ������
		filename = CommonUtils.uuid() + "_" + filename;
		// У���ļ����Ƶ���չ��
		if(!filename.toLowerCase().endsWith(".jpg")) {
			error("�ϴ���ͼƬ��չ��������JPG", request, response);
			return;
		}
		// У��ͼƬ�ĳߴ�
		// �����ϴ���ͼƬ����ͼƬnew��ͼƬ����Image��Icon��ImageIcon��BufferedImage��ImageIO
		/*
		 * ����ͼƬ��
		 * ��ȡ��ʵ·��
		 */
		String savepath = this.getServletContext().getRealPath("/book_img");
		/*
		 * ����Ŀ���ļ�
		 */
		File destFile = new File(savepath, filename);
		/*
		 * �����ļ�
		 */
		try {
			fileItem.write(destFile);//�������ʱ�ļ��ض���ָ����·������ɾ����ʱ�ļ�
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// У��ߴ�
		// ʹ���ļ�·������ImageIcon
		ImageIcon icon = new ImageIcon(destFile.getAbsolutePath());
		// ͨ��ImageIcon�õ�Image����
		Image image = icon.getImage();
		// ��ȡ����������У��
		if(image.getWidth(null) > 350 || image.getHeight(null) > 350) {
			error("���ϴ���ͼƬ�ߴ糬����350*350��", request, response);
			destFile.delete();//ɾ��ͼƬ
			return;
		}
		
		// ��ͼƬ��·�����ø�book����
		book.setImage_w("book_img/" + filename);
		
		


		// ��ȡ�ļ���
		fileItem = fileItemList.get(2);//��ȡСͼ
		filename = fileItem.getName();
		// ��ȡ�ļ�������Ϊ����������ϴ��ľ���·��
		index = filename.lastIndexOf("\\");
		if(index != -1) {
			filename = filename.substring(index + 1);
		}
		// ���ļ�������uuidǰ׺�������ļ�ͬ������
		filename = CommonUtils.uuid() + "_" + filename;
		// У���ļ����Ƶ���չ��
		if(!filename.toLowerCase().endsWith(".jpg")) {
			error("�ϴ���ͼƬ��չ��������JPG", request, response);
			return;
		}
		// У��ͼƬ�ĳߴ�
		// �����ϴ���ͼƬ����ͼƬnew��ͼƬ����Image��Icon��ImageIcon��BufferedImage��ImageIO
		/*
		 * ����ͼƬ��
		 * ��ȡ��ʵ·��
		 */
		savepath = this.getServletContext().getRealPath("/book_img");
		/*
		 * ����Ŀ���ļ�
		 */
		destFile = new File(savepath, filename);
		/*
		 * �����ļ�
		 */
		try {
			fileItem.write(destFile);//�������ʱ�ļ��ض���ָ����·������ɾ����ʱ�ļ�
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// У��ߴ�
		// ʹ���ļ�·������ImageIcon
		icon = new ImageIcon(destFile.getAbsolutePath());
		// ͨ��ImageIcon�õ�Image����
		image = icon.getImage();
		// ��ȡ����������У��
		if(image.getWidth(null) > 350 || image.getHeight(null) > 350) {
			error("���ϴ���ͼƬ�ߴ糬����350*350��", request, response);
			destFile.delete();//ɾ��ͼƬ
			return;
		}
		
		// ��ͼƬ��·�����ø�book����
		book.setImage_b("book_img/" + filename);
		
		
		
		
		// ����service��ɱ���
		book.setBid(CommonUtils.uuid());
		BookService bookService = new BookService();
		bookService.add(book);
		
		// ����ɹ���Ϣת����msg.jsp
		request.setAttribute("msg", "����ͼ��ɹ���");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
	}
	
	/*
	 * ���������Ϣ��ת����add.jsp
	 */
	private void error(String msg, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("msg", msg);
		request.setAttribute("parents", new CategoryService().findParents());//����һ������
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").
				forward(request, response);
	}
}