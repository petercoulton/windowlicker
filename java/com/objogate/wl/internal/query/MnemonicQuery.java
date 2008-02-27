package com.objogate.wl.internal.query;

import org.hamcrest.Matcher;

public interface MnemonicQuery {
    void mnemonic(Matcher<Character> character);
}
