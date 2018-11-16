package org.vaadin.easyuploads.demoandtestapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.easyuploads.FileBuffer;
import org.vaadin.easyuploads.MultiFileUpload;

import com.google.common.io.Files;
import com.vaadin.annotations.Theme;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.easyuploads.UploadField;

@Theme("valo")
public class MultiFileUploads extends AbstractTest {

    @SuppressWarnings("serial")
    @Override
    public Component getTestComponent() {
        VerticalLayout mainWindow = new VerticalLayout();

        MultiFileUpload multiFileUpload = new MultiFileUpload() {
            @Override
            protected void handleFile(File file, String fileName,
                                      String mimeType, long length) {
                String msg = fileName + " uploaded. Saved to temp file "
                        + file.getAbsolutePath() + " (size " + length
                        + " bytes)";
                Notification.show(msg);
            }
        };
        multiFileUpload.setCaption("MultiFileUpload");
        mainWindow.addComponent(multiFileUpload);
        mainWindow.addComponent(hr());

        MultiFileUpload multiFileUploadLimited = new MultiFileUpload() {
            @Override
            protected void handleFile(File file, String fileName,
                                      String mimeType, long length) {
                String msg = fileName + " uploaded. Saved to temp file "
                        + file.getAbsolutePath() + " (size " + length
                        + " bytes)";
                Notification.show(msg);
            }
        };
        multiFileUploadLimited.setCaption(
                "MultiFileUpload limited to < 100 000 bytes (~ 97 KB), images, and 5 files");
        multiFileUploadLimited.setMaxFileSize(100000);
        multiFileUploadLimited.setAcceptFilter("image/*");
        multiFileUploadLimited.setMaxFileCount(5);
        mainWindow.addComponent(multiFileUploadLimited);
        mainWindow.addComponent(hr());

        MultiFileUpload multiFileUpload2 = new MultiFileUpload() {
            @Override
            protected void handleFile(File file, String fileName,
                                      String mimeType, long length) {
                String msg = fileName + " uploaded. Saved to file "
                        + file.getAbsolutePath() + " (size " + length
                        + " bytes)";

                Notification.show(msg);
            }

            @Override
            protected FileBuffer createReceiver() {
                FileBuffer receiver = super.createReceiver();
                /*
                 * Make receiver not to delete files after they have been
                 * handled by #handleFile().
                 */
                receiver.setDeleteFiles(false);
                return receiver;
            }
        };
        multiFileUpload2.setCaption("MultiFileUpload (with root dir)");
        multiFileUpload2.setRootDirectory(Files.createTempDir().toString());
        mainWindow.addComponent(multiFileUpload2);

        mainWindow.addComponent(hr());
        MultiFileUpload multiFileUpload3 = new SlowMultiFileUpload();
        multiFileUpload3.setCaption("MultiFileUpload (simulated slow network)");
        multiFileUpload3.setUploadButtonCaption("Choose File(s)");
        mainWindow.addComponent(multiFileUpload3);

        FormLayout maxSizeMultiUploadLayout = new FormLayout();
        maxSizeMultiUploadLayout.setCaption("MultiFileUpload with maxSize");
        mainWindow.addComponent(maxSizeMultiUploadLayout);
        
        final TextField maxSizeField = new TextField();
        maxSizeField.setCaption("Max size : ");
        maxSizeMultiUploadLayout.addComponent(maxSizeField);
        final MultiFileUpload multiFileUploadWithMaxSize = new MultiFileUpload() {
            @Override
            protected void handleFile(File file, String fileName, String mimeType, long length) {
                String msg = fileName + " uploaded. Saved to temp file " + file.getAbsolutePath() + " (size " + length
                        + " bytes)";
                Notification.show(msg);
            }
        };
        
        maxSizeField.addValueChangeListener(e-> {
            multiFileUpload.setMaxFileSize(Integer.parseInt(e.getValue()));
        });
        maxSizeField.setValue("1048576");

        maxSizeMultiUploadLayout.addComponent(multiFileUploadWithMaxSize);
        mainWindow.addComponent(hr());

        return mainWindow;
    }

    @Override
    protected void setup() {
        super.setup();
        content.setSizeUndefined();
    }

    class SlowMultiFileUpload extends MultiFileUpload {

        @Override
        protected void handleFile(File file, String fileName, String mimeType,
                                  long length) {
            String msg = fileName + " uploaded.";
            Notification.show(msg);
        }

        @Override
        protected FileBuffer createReceiver() {
            return new FileBuffer() {
//                @Override
//                public FileFactory getFileFactory() {
//                    return SlowMultiFileUpload.this.getFileFactory();
//                }

                @Override
                public OutputStream receiveUpload(String filename,
                                                  String MIMEType) {
                    final OutputStream receiveUpload = super.receiveUpload(
                            filename, MIMEType);
                    OutputStream slow = new OutputStream() {
                        private int slept;
                        private int written;

                        @Override
                        public void write(int b) throws IOException {
                            receiveUpload.write(b);
                            written++;
                            if (slept < 60000 && written % 1024 == 0) {
                                int sleep = 5;
                                slept += sleep;
                                try {
                                    Thread.sleep(sleep);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    return slow;
                }

                @Override
                public void setValue(byte[] newValue) {

                }

                @Override
                public void setLastMimeType(String mimeType) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void setLastFileName(String fileName) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public InputStream getContentAsStream() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean isEmpty() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public long getLastFileSize() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String getLastMimeType() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public String getLastFileName() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

            };
        }

    }

    private Component hr() {
        Label label = new Label("<hr>", ContentMode.HTML);
        return label;
    }

}
