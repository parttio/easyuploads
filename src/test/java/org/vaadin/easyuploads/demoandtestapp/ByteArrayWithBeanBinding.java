package org.vaadin.easyuploads.demoandtestapp;

import java.io.*;

import org.vaadin.easyuploads.*;
import org.vaadin.easyuploads.UploadField.FieldType;
import org.vaadin.easyuploads.UploadField.StorageMode;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.layouts.MVerticalLayout;

@Theme("valo")
public class ByteArrayWithBeanBinding extends AbstractTest {

    private BeanFieldGroup<BeanWithFileEntity> bfg;

    public static class FileEntity {

        byte[] value;

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }

        String mimeType;

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        @Override
        public String toString() {
            String string;
            if (value == null) {
                string = "null";
            } else {
                string = new String(value);
                if (string.length() > 100) {
                    string = string.substring(0, 100);
                }

            }
            return "FileEntity{" + "value=" + string + ", mimeType=" + mimeType + '}';
        }

    }

    public static class BeanWithFileEntity {

        private FileEntity fileEntity = new FileEntity();

        public FileEntity getFileEntity() {
            return fileEntity;
        }

        public void setFileEntity(FileEntity fileEntity) {
            this.fileEntity = fileEntity;
        }

        @Override
        public String toString() {
            return "BeanWithFileEntity{" + "fileEntity=" + fileEntity + '}';
        }

    }

    UploadField value = new UploadField(StorageMode.MEMORY);

    CustomFieldEditingBothMimeAndContent fileEntity = new CustomFieldEditingBothMimeAndContent();

    @Override
    public Component getTestComponent() {

        value.setFieldType(FieldType.BYTE_ARRAY);

        final FileEntity entity = new FileEntity();

        value.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show("You could change stuff here as well, mimetype:" + value.getLastMimeType());
            }
        });

        entity.setValue("Foobar".getBytes());

        BeanFieldGroup.bindFieldsUnbuffered(entity, this);

        Button b = new Button("Show entity value");
        b.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show(entity.toString());
            }
        });

        fileEntity.setCaption("Nested class editor example");
        
        fileEntity.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show("ValueChangeListener", "In custom field", Notification.Type.TRAY_NOTIFICATION);
            }
        });

        final BeanWithFileEntity beanWithFileEntity = new BeanWithFileEntity();

        bfg = BeanFieldGroup.bindFieldsUnbuffered(beanWithFileEntity, this);

        Button showValue = new Button("Show entity value");
        showValue.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show(beanWithFileEntity.toString());
            }
        });

        Button rebind = new Button("Bind with value");
        rebind.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                
                FileEntity fe = new FileEntity();
                fe.setValue("Simple text example".getBytes());
                fe.setMimeType("text/plain");
                beanWithFileEntity.setFileEntity(fe);
                BeanFieldGroup.bindFieldsUnbuffered(beanWithFileEntity, ByteArrayWithBeanBinding.this);
            }
        });

        return new MVerticalLayout(value, b, fileEntity, showValue, rebind);
    }

    @Override
    protected void setup() {
        super.setup();
        content.setSizeUndefined();
    }

    private Component hr() {
        Label label = new Label("<hr>", Label.CONTENT_XHTML);
        return label;
    }

    /**
     * An example of custom field editing for example DB entity containing the
     * content of the file and its mime type.
     */
    public static class CustomFieldEditingBothMimeAndContent extends CustomField<FileEntity> {

        private UploadField field = new UploadField();
        private FileEntity fileEntity;

        public CustomFieldEditingBothMimeAndContent() {
            field.setFieldType(FieldType.BYTE_ARRAY);

            field.addValueChangeListener(new ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    fileEntity.setValue((byte[]) field.getValue());
                    fileEntity.setMimeType(field.getLastMimeType());
                    CustomFieldEditingBothMimeAndContent.this.fireValueChange(false);
                }
            });
        }

        @Override
        protected Component initContent() {
            return field;
        }

        @Override
        protected void setInternalValue(FileEntity newValue) {
            fileEntity = newValue;

            field.setLastMimeType(fileEntity.getMimeType());

            // UploadField don't properly support setValue, but only databinding
            BeanItem beanItem = new BeanItem(newValue);
            field.setPropertyDataSource(beanItem.getItemProperty("value"));

            super.setInternalValue(newValue);
        }

        @Override
        public Class<? extends FileEntity> getType() {
            return FileEntity.class;
        }

    }

}
