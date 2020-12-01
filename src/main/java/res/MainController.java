package res;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class MainController {

    @FXML
    public Label welcome;
    @FXML
    public Label bal;
    @FXML
    public GridPane mainScreen;
    @FXML
    public Label nameLab, surLab, cnfPIN, enterPIN;
    @FXML
    public TextField nameF, surF, firstPIN, secondPIN;
    @FXML
    public Button PINEnterNew;

    public Account account;

    public int state; //0 create, 1 login, 2 main screen, 3 withdraw, 4 pin change, 5 details, 6 deposit

    @FXML
    public AnchorPane newAccountPane;
    @FXML
    public PasswordField passwordBack;
    @FXML
    public AnchorPane loginPane;
    @FXML
    public GridPane withdrawDep;
    @FXML
    public Label infoLabel;
    @FXML
    public AnchorPane details;
    @FXML
    public TextField detailsName;
    @FXML
    public AnchorPane changePINPane;
    @FXML
    public PasswordField currPIN, newPIN, newPINConf;
    @FXML
    public Button button1;
    @FXML
    public AnchorPane customPane;
    @FXML
    public TextField customAmount;


    public void initialize() {
        hideAll();
        account = read();
    }

    public void withdraw() {
        setState(3);
    }

    public void deposit() {
        setState(6);
    }

    public void pinChange() {
        setState(4);
    }

    public void details() {
        setState(5);
    }

    public void shutdown() {
        Stage stage = (Stage) welcome.getScene().getWindow();
        stage.close();
    }

    private Account read() {
        try {
            InputStream is = Files.newInputStream(Path.of("new.obj"));
            ObjectInputStream ois = new ObjectInputStream(is);
            setState(1);
            return (Account) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No account!");
        }
        setState(0);
        return null;
    }

    private void setState(int i) {
        switch (i) {
            case 0 -> {
                state = 0;
                welcome.setText("Welcome, please register");
                newAccountPane.setVisible(true);
            }
            case 1 -> {
                state = 1;
                hideAll();
                loginPane.setVisible(true);
            }
            case 2 -> {
                state = 2;
                hideAll();
                welcome.setText("Hi, " + account.name);
                refresh();
                bal.setVisible(true);
                mainScreen.setVisible(true);
            }
            case 3 -> {
                state = 3;
                hideAll();
                refresh();
                withdrawDep.setVisible(true);
                infoLabel.setVisible(true);
                infoLabel.setText("Select one of the following options");
            }
            case 4 -> {
                state = 4;
                hideAll();
                refresh();
                changePINPane.setVisible(true);
            }
            case 5 -> {
                state = 5;
                hideAll();
                refresh();
                detailsName.setText(account.name + " " + account.sur);
                details.setVisible(true);
            }
            case 6 -> {
                state = 6;
                hideAll();
                refresh();
                withdrawDep.setVisible(true);
                infoLabel.setVisible(true);
                infoLabel.setText("Select one of the following options");
            }
        }
    }

    private void refresh() {
        bal.setText("Current balance: " + account.balance + "PLN");
    }

    private void hideAll() {
        mainScreen.setVisible(false); // status 2 - main
        newAccountPane.setVisible(false); // status 0 - new account
        loginPane.setVisible(false); // status 1 - login
        withdrawDep.setVisible(false); // status 3/6 - withdraw/deposit
        infoLabel.setVisible(false); // status 3/6 - withdraw/deposit
        details.setVisible(false); // status 5 - details
        changePINPane.setVisible(false); // status 4 - change PIN
        customPane.setVisible(false); // status ? - custom w/d
    }

    private void write() {
        try {
            FileOutputStream fos = new FileOutputStream("new.obj");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(account);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createAccount() {
        if (firstPIN.getText().equals(secondPIN.getText()) && firstPIN.getText().matches("^[0-9]{4}$")) {
            account = new Account(Integer.parseInt(firstPIN.getText()), nameF.getText(), surF.getText());
            write();
            setState(2);
        } else {
            PINDoesNotMatch();
        }
    }

    private void PINDoesNotMatch() {
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                infoLabel.setVisible(true);
                infoLabel.setTextFill(Color.RED);
                infoLabel.setText("Wrong PIN");
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            infoLabel.setTextFill(Color.BLACK);
            infoLabel.setVisible(false);
        });
        thread.start();
    }

    public void back() {
        if (state >= 2)
            setState(2);
    }

    public void login() {
        if (!passwordBack.getText().equals("") && Integer.parseInt(passwordBack.getText()) == account.PIN) {
            setState(2);
        } else {
            PINDoesNotMatch();
        }
    }

    public void w10() {
        switch (state) {
            case 3:
                if (account.balance >= 10) {
                    account.balance = account.balance - 10;
                } else {
                    insufficientFunds();
                }
                break;
            case 6:
                account.balance = account.balance + 10;
                break;
        }
        refresh();
        write();
    }

    private void insufficientFunds() {
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                infoLabel.setTextFill(Color.RED);
                infoLabel.setText("Insufficient funds on your account");
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("nothing");
            }
            Platform.runLater(() -> {
                infoLabel.setText("Select one of the following options");
                infoLabel.setTextFill(Color.BLACK);
            });
        });
        thread.start();
    }

    public void w20() {
        switch (state) {
            case 3:
                if (account.balance >= 20) {
                    account.balance = account.balance - 20;
                } else {
                    insufficientFunds();
                }
                break;
            case 6:
                account.balance = account.balance + 20;
                break;
        }
        refresh();
        write();
    }

    public void w50() {
        switch (state) {
            case 3:
                if (account.balance >= 50) {
                    account.balance = account.balance - 50;
                } else {
                    insufficientFunds();
                }
                break;
            case 6:
                account.balance = account.balance + 50;
                break;
        }
        refresh();
        write();
    }

    public void w100() {
        switch (state) {
            case 3:
                if (account.balance >= 100) {
                    account.balance = account.balance - 100;
                } else {
                    insufficientFunds();
                }
                break;
            case 6:
                account.balance = account.balance + 100;
                break;
        }
        refresh();
        write();
    }

    public void w500() {
        switch (state) {
            case 3:
                if (account.balance >= 500) {
                    account.balance = account.balance - 500;
                } else {
                    insufficientFunds();
                }
                break;
            case 6:
                account.balance = account.balance + 500;
                break;
        }
        refresh();
        write();
    }

    public void custom() {
        hideAll();
        refresh();
        write();
        customPane.setVisible(true);
    }

    public void changePIN() {
        if (Integer.parseInt(currPIN.getText()) == account.PIN
                && Integer.parseInt(newPIN.getText()) == Integer.parseInt(newPINConf.getText())
                && newPIN.getText().matches("^[0-9]{4}$")) {
            account.setPIN(Integer.parseInt(newPIN.getText()));
            write();
            setState(2);
        } else {
            PINDoesNotMatch();
        }
    }

    public void wCustom() {
        switch (state) {
            case 3:
                if (account.balance >= Integer.parseInt(customAmount.getText())) {
                    account.balance = account.balance - Integer.parseInt(customAmount.getText());
                } else {
                    insufficientFunds();
                }
                break;
            case 6:
                account.balance = account.balance + Integer.parseInt(customAmount.getText());
                break;
        }
        setState(2);
        refresh();
        write();
    }
}
