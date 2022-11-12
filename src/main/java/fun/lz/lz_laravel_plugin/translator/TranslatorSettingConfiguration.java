package fun.lz.lz_laravel_plugin.translator;

import cn.hutool.core.util.StrUtil;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TranslatorSettingConfiguration implements Configurable {
    private final JComponent component;
    private final JTextField appID;
    private final JTextField securityKey;
    private final static String appIDHint = "请输入 appID";
    private final static String securityKeyHint = "请输入 securityKey";

    public TranslatorSettingConfiguration() {
        this.component = new JPanel();
        this.component.setLayout(new GridLayout(15, 1));

        this.appID = new JTextField();
        this.securityKey = new JTextField();

        var setting = TranslatorSetting.getInstance();

        if (StrUtil.isNotBlank(setting.appID)) {
            this.appID.setText(setting.appID);
        } else {
            this.appID.setForeground(JBColor.GRAY);
            this.appID.addFocusListener(new TextFieldListener(this.appID, appIDHint));
        }

        if (StrUtil.isNotBlank(setting.securityKey)) {
            this.securityKey.setText(setting.securityKey);
        } else {
            this.securityKey.setForeground(JBColor.GRAY);
            this.securityKey.addFocusListener(new TextFieldListener(this.securityKey, securityKeyHint));
        }

        this.component.add(appID);
        this.component.add(securityKey);
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Translator";
    }

    @Override
    public @Nullable JComponent createComponent() {
        return component;
    }

    @Override
    public boolean isModified() {
        return true;
    }

    /**
     * 点击配置页面中的 apply 按钮或者 OK 按钮，会调用该方法，在该方法中保存配
     */
    @Override
    public void apply() throws ConfigurationException {
        TranslatorSetting.getInstance().appID = appID.getText();
        TranslatorSetting.getInstance().securityKey = securityKey.getText();
    }

    static class TextFieldListener implements FocusListener {
        private final JTextField textField;
        private final String defaultHint;

        public TextFieldListener(JTextField textField, String defaultHint) {
            this.textField = textField;
            this.defaultHint = defaultHint;
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (textField.getText().equals(defaultHint)) {
                textField.setText("");
                textField.setForeground(JBColor.BLACK);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (textField.getText().equals("")) {
                textField.setText(defaultHint);
                textField.setForeground(JBColor.GRAY);
            }
        }
    }
}
