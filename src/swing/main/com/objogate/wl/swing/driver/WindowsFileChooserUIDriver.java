package com.objogate.wl.swing.driver;

import javax.swing.JToggleButton;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

class WindowsFileChooserUIDriver extends MetalFileChooserUIDriver {
    private static final String DESKTOP_BUTTON_TEXT = "Desktop";
    private static final String HOME_BUTTON_TEXT = "My Documents";

    public WindowsFileChooserUIDriver(JFileChooserDriver jFileChooserDriver) {
        super(jFileChooserDriver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void desktop() {
        new AbstractButtonDriver<JToggleButton>(parentOrOwner, JToggleButton.class,
                new HtmlToggleButtonMatcher(DESKTOP_BUTTON_TEXT)).click();
    }

    @Override
    public void home() {
        throw new UnsupportedOperationException("There is no 'Home' button in the Windows L&F");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void documents() {
        new AbstractButtonDriver<JToggleButton>(parentOrOwner, JToggleButton.class,
                new HtmlToggleButtonMatcher(HOME_BUTTON_TEXT)).click();
    }

    private class HtmlToggleButtonMatcher extends TypeSafeMatcher<JToggleButton> {
        private String desktopButtonText;

        public HtmlToggleButtonMatcher(String desktopButtonText) {
            this.desktopButtonText = desktopButtonText;
        }

        @Override
        public boolean matchesSafely(JToggleButton jButton) {
            return jButton.getText().equals(wrapInWindowsHtml(desktopButtonText));
        }

        public void describeTo(Description description) {
            description.appendText("Button with text '" + wrapInWindowsHtml(desktopButtonText) + "'");
        }

        private String wrapInWindowsHtml(String buttonText) {
            return "<html><center>" + buttonText + "</center></html>";
        }
    }
}
