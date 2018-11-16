package org.vaadin.easyuploads.demoandtestapp;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.easyuploads.UploadField;

@Theme("valo")
public class ByteArrayWithBeanBinding extends AbstractTest {

    private Binder<BeanWithFileEntity> bfg;

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

    UploadField value = new UploadField();

    CustomFieldEditingBothMimeAndContent fileEntity = new CustomFieldEditingBothMimeAndContent();

    @Override
    public Component getTestComponent() {

        final FileEntity entity = new FileEntity();

        value.addValueChangeListener(event -> Notification.show("You could change stuff here as well, mimetype:" + value.getLastMimeType()));

        entity.setValue("Foobar".getBytes());

        Button b = new Button("Show entity value");
        b.addClickListener(event -> Notification.show(entity.toString()));

        fileEntity.setCaption("Nested class editor example");

        fileEntity.addValueChangeListener(event -> Notification.show("ValueChangeListener", "In custom field", Notification.Type.TRAY_NOTIFICATION));

        final BeanWithFileEntity beanWithFileEntity = new BeanWithFileEntity();

        bfg = new Binder<>(BeanWithFileEntity.class);
        bfg.bindInstanceFields(this);
        bfg.setBean(beanWithFileEntity);

        Button showValue = new Button("Show entity value");
        showValue.addClickListener(event -> Notification.show(beanWithFileEntity.toString()));

        Button rebind = new Button("Bind with value");
        rebind.addClickListener(event -> {
            FileEntity fe = new FileEntity();
            fe.setValue("Simple text example".getBytes());
            fe.setMimeType("text/plain");
            beanWithFileEntity.setFileEntity(fe);
            bfg.removeBean();
            bfg.setBean(beanWithFileEntity);
        });

        return new VerticalLayout(value, b, fileEntity, showValue, rebind);
    }

    @Override
    protected void setup() {
        super.setup();
        content.setSizeUndefined();
    }

    private Component hr() {
        Label label = new Label("<hr>", ContentMode.HTML);
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

            field.addValueChangeListener(event -> {
                fileEntity.setValue((byte[]) field.getValue());
                fileEntity.setMimeType(field.getLastMimeType());
                CustomFieldEditingBothMimeAndContent.this.fireEvent(new ValueChangeEvent<>(event.getComponent(), this, null, false));
            });
        }

        @Override
        protected Component initContent() {
            return field;
        }

        @Override
        protected void doSetValue(FileEntity newValue) {
            fileEntity = newValue;

            if (fileEntity != null) {
                field.setLastMimeType(fileEntity.getMimeType());
            }

            if (newValue != null) {
                // UploadField don't properly support setValue, but only databinding
                field.setValue(newValue.getValue());
            }
        }

        /**
         * @see com.vaadin.data.HasValue#getValue()
         */
        @Override
        public FileEntity getValue() {
            return fileEntity;
        }

    }

}
