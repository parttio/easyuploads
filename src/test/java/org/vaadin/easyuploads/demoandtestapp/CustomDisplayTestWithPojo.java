package org.vaadin.easyuploads.demoandtestapp;

import org.vaadin.easyuploads.ImagePreviewField;
import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritinv7.BeanBinder;
import org.vaadin.viritinv7.MBeanFieldGroup;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@Theme("valo")
public class CustomDisplayTestWithPojo extends AbstractTest {

    private MBeanFieldGroup<Pojo> bfg;

    public static class Pojo {

        private String foo;
        private byte[] photo;

        public String getFoo() {
            return foo;
        }

        public void setFoo(String foo) {
            this.foo = foo;
        }

        public byte[] getPhoto() {
            return photo;
        }

        public void setPhoto(byte[] photo) {
            this.photo = photo;
        }

    }

    Pojo pojo = new Pojo();

    ImagePreviewField photo = new ImagePreviewField();

    @Override
    public Component getTestComponent() {
        VerticalLayout layout = new MVerticalLayout();
        photo.setCaption("Custom prview for images.");

        Button button = new Button("Show photo state");
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                final byte[] photo1 = pojo.getPhoto();
                if (photo1 == null) {
                    Notification.show("EMPTY");
                } else {
                    String string = new String(photo1);
                    if (string.length() > 100) {
                        string = string.substring(0, 99);
                    }
                    Notification.show(string);
                }
            }
        });

        Button load = new Button("Load photo");
        load.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                bfg.unbind();
                InputStream resourceAsStream = getClass().getResourceAsStream("/boat.png");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    IOUtils.copy(resourceAsStream, byteArrayOutputStream);
                    pojo.setPhoto(byteArrayOutputStream.toByteArray());
                    bfg = BeanBinder.bind(pojo, CustomDisplayTestWithPojo.this);
                } catch (IOException ex) {
                    Logger.getLogger(CustomDisplayTestWithPojo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Button clear = new Button("clear value", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                pojo.setPhoto(null);
                bfg = BeanBinder.bind(pojo, CustomDisplayTestWithPojo.this);
            }
        });

        bfg = BeanBinder.bind(pojo, this);

        layout.addComponents(photo, new MHorizontalLayout( button, load, clear));
        return layout;
    }

}
