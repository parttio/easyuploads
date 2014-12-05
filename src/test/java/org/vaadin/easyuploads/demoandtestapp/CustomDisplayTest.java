package org.vaadin.easyuploads.demoandtestapp;

import java.io.*;

import org.vaadin.easyuploads.*;
import org.vaadin.easyuploads.UploadField.*;

import com.vaadin.server.*;
import com.vaadin.ui.*;

public class CustomDisplayTest extends AbstractTest
{

   @Override
   Component getTestComponent()
   {
      VerticalLayout layout = new VerticalLayout();
      ImagePreviewField upload = new ImagePreviewField();
      upload.setCaption("Custom prview for images.");
      upload.setAcceptFilter("image/*");
      upload.setFieldType(FieldType.FILE);
      layout.addComponent(upload);
      return layout;
   }

   public class ImagePreviewField extends UploadField
   {
      @Override
      protected Component createDisplayComponent()
      {
	 return new Image();
      }

      @Override
      protected void updateDisplayComponent()
      {
	 Image image = (Image) display;
	 Object value = getValue();
	 File file = new File(value.toString());
	 image.setHeight("100px");
	
	 FileResource resource = new FileResource(file);
	 image.setSource(resource);

	 if (display.getParent() == null)
	 {
	    getRootLayout().addComponent(display);
	 }
      }
   }
}
