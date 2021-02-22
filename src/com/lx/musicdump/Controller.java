package com.lx.musicdump;

import com.lx.musicdump.music.MusicFile;
import com.lx.musicdump.music.kugoo.KugooMusicFile;
import com.lx.musicdump.music.kuwo.KuwoMusicFile;
import com.lx.musicdump.music.neteast.NeteastMusicFile;
import com.lx.musicdump.music.tencent.QQMusicFile;
import com.lx.musicdump.music.xiami.XiamiMusicFile;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.*;

public class Controller {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TableView<File> tableView;

    @FXML
    private Label labelCurrent;

    @FXML
    private Label labelCount;

    @FXML
    private TextField textFieldSrcPathname;

    @FXML
    private TextField textFieldDstPathname;

    @FXML
    private Button buttonSubmit;

    public Controller() {
        System.out.println("Controller");

        System.out.println("Controller labelCount = " + labelCount);
    }

    @FXML
    private void initialize() {
        System.out.println("initialize");

        System.out.println("initialize tableView = " + tableView);
        System.out.println("initialize labelCount = " + labelCount);

        initTableView();

        setSrcPathname(null);
        setDstPathname(null);
    }

    @FXML
    private void onAction1(ActionEvent actionEvent) {
        System.out.println("onAction1");

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(((Button) actionEvent.getSource()).getScene().getWindow());

        if (file == null) {
            return;
        }

        observableMap.clear();

        setSrcPathname(null);
        setDstPathname(null);

        setSrcPathname(file.getPath());
        setDstPathname(file.getPath());
    }

    @FXML
    private void onAction2(ActionEvent actionEvent) {
        System.out.println("onAction2");

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File file = directoryChooser.showDialog(((Button) actionEvent.getSource()).getScene().getWindow());

        if (file == null) {
            return;
        }

        setDstPathname(file.getPath());
    }

    private ObservableMap<File, Status> observableMap = FXCollections.observableMap(new HashMap<File, Status>());

    @FXML
    private void onActionSubmit(ActionEvent actionEvent) {
        System.out.println("onActionSubmit");

        ObservableList<File> items = tableView.getItems();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                observableMap.clear();

                for (File file : items) {
                    observableMap.put(file, Status.WAIT);
                }
                tableView.refresh();

                for (int i = 0; i < items.size(); i++) {
                    File file = items.get(i);

                    observableMap.put(file, Status.PROGRESS);
                    tableView.refresh();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    MusicFile musicFile = MusicFile.read(file.getPath());
                    if (musicFile.isValid()) {
                        musicFile.setMode(MusicFile.Mode.BOTH);
                        musicFile.setSrcParent(textFieldSrcPathname.getText());
                        musicFile.setDstParent(textFieldDstPathname.getText());

                        musicFile.dump();
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    observableMap.put(file, Status.DONE);
                    tableView.refresh();

                    int finalI = i;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            labelCurrent.setText(String.valueOf(finalI + 1));
                            progressBar.setProgress((finalI + 1.0) / items.size());
                        }
                    });
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void initTableView() {
        TableColumn tableColumnIcon = tableView.getColumns().get(0);
        TableColumn tableColumnName = tableView.getColumns().get(1);
        TableColumn tableColumnExtension = tableView.getColumns().get(2);
        TableColumn tableColumnSize = tableView.getColumns().get(3);
        TableColumn tableColumnPath = tableView.getColumns().get(4);
//        TableColumn tableColumnProgressIndicator = tableView.getColumns().get(5);
        TableColumn tableColumnStatus = tableView.getColumns().get(5);

        tableColumnIcon.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, ?>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<File, ?> param) {
//                System.out.println("value = " + param.getValue());
                File file = param.getValue();
                String extension = FilenameUtils.getExtension(file.getName());
                return new SimpleStringProperty(extension);
            }
        });
        tableColumnIcon.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {

                TableCell<File, String> tableCell = new TableCell<File, String>() {

                    private ImageView imageView;

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {

                            if (imageView == null) {
                                imageView = new ImageView();
                            }

                            imageView.setFitWidth(18);
                            imageView.setFitHeight(18);

//                            System.out.println(getImageUrl(item));
//                            imageView.setImage(new Image(getImageUrl(item)));

//                            System.out.println("getImageUrl(item) = " + getImageUrl(item));

                            String url = "/extension/" + getImageUrl(item);
//                            System.out.println("url = " + url);
                            Image image = new Image(url);
                            imageView.setImage(image);

                            setGraphic(imageView);
                        }
                    }
                };

                return tableCell;
            }
        });

        tableColumnExtension.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, ?>, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<File, ?> param) {
//                System.out.println("value = " + param.getValue());
                File file = param.getValue();
                String extension = FilenameUtils.getExtension(file.getName());
                return new SimpleStringProperty(extension);
            }
        });

        tableColumnSize.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, ?>, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<File, ?> param) {
//                System.out.println("value = " + param.getValue());
                File file = param.getValue();
                String displaySize = FileUtils.byteCountToDisplaySize(file.length());
                return new SimpleStringProperty(displaySize);
            }
        });

        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnPath.setCellValueFactory(new PropertyValueFactory<>("path"));

        tableColumnStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<File, ?>, ObservableValue>() {
            @Override
            public ObservableValue call(TableColumn.CellDataFeatures<File, ?> param) {
                File file = param.getValue();
                Status status = observableMap.get(file);
                return new SimpleStringProperty(status != null ? status.toString() : null);
            }
        });

        tableColumnStatus.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn param) {
                TableCell<File, String> tableCell = new TableCell<File, String>() {

                    private ImageView imageView;

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

//                        System.out.println("updateItem item = " + item);
//                        System.out.println("updateItem empty = " + empty);

                        if (empty) {
                            setGraphic(null);
                        } else {

                            if (imageView == null) {
                                imageView = new ImageView();
                            }

                            imageView.setFitWidth(18);
                            imageView.setFitHeight(18);

                            Image image = null;
                            if (item != null) {
                                String url = "/status/" + getStatusImageUrl(Status.valueOf(item));
                                System.out.println("url = " + url);
                                image = new Image(url);
                            }
                            imageView.setImage(image);

                            setGraphic(imageView);
                        }
                    }
                };

                return tableCell;
            }
        });
    }

    private void setSrcPathname(String pathname) {
        textFieldSrcPathname.setText(pathname);

        Collection<File> files = Collections.EMPTY_LIST;
        if (pathname != null) {
            String[] extensions = MusicFile.getExtensions();
            files = FileUtils.listFiles(new File(pathname), extensions, true);
        }
        ObservableList<File> items = FXCollections.observableArrayList(files);
        tableView.setItems(items);

        labelCurrent.setText(String.valueOf(0));
        labelCount.setText(String.valueOf(files.size()));

        buttonSubmit.setDisable(files.isEmpty());
    }

    private void setDstPathname(String pathname) {
        textFieldDstPathname.setText(pathname);
    }

    private static String getImageUrl(String extension) {

        Class<? extends MusicFile> _class = MusicFile.getClass(extension);

        Map<Class<? extends MusicFile>, String> map = new HashMap<>();
        map.put(KugooMusicFile.class, "kugoo.png");
        map.put(KuwoMusicFile.class, "kuwo.png");
        map.put(NeteastMusicFile.class, "neteast.png");
        map.put(QQMusicFile.class, "qq.png");
        map.put(XiamiMusicFile.class, "xiami.png");

//        return "file:./assets/icon" + File.separator + map.get(_class);
        return map.get(_class);
    }

    enum Status {
        WAIT,
        PROGRESS,
        DONE,
        ERROR
    }

    private static String getStatusImageUrl(Status status) {
        Map<Status, String> map = new HashMap<>();
        map.put(Status.PROGRESS, "baseline_autorenew_black_18dp.png");
        map.put(Status.DONE, "baseline_check_circle_outline_black_18dp.png");
        map.put(Status.ERROR, "baseline_error_outline_black_18dp.png");
        map.put(Status.WAIT, "baseline_hourglass_empty_black_18dp.png");
        return map.get(status);
    }
}
