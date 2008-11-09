package com.objogate.wl.swing.internal.query;

import org.hamcrest.Matcher;

public interface MnemonicQuery {
    void mnemonic(Matcher<Character> character);
}
