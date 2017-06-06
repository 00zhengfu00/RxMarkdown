/*
 * Copyright (C) 2016 yydcdut (yuyidong2015@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yydcdut.rxmarkdown.syntax.text;

import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;

import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.chain.GrammarDoElseChain;
import com.yydcdut.rxmarkdown.chain.GrammarMultiChains;
import com.yydcdut.rxmarkdown.chain.GrammarSingleChain;
import com.yydcdut.rxmarkdown.chain.IChain;
import com.yydcdut.rxmarkdown.chain.MultiGrammarsChain;
import com.yydcdut.rxmarkdown.syntax.Syntax;
import com.yydcdut.rxmarkdown.syntax.SyntaxFactory;

/**
 * This factory's purpose is parsing content <b>correctly</b>, as the same time, it destroys the integrity of the content.
 * This factory will delete the key words of markdown grammar in content.
 * So, hope that it will be used in TextView, not in EditText.
 * <p>
 * Created by yuyidong on 16/5/12.
 */
public class TextFactory implements SyntaxFactory {
    private static final String NEWLINE = "\n";
    private RxMDConfiguration mRxMDConfiguration;
    private IChain mLineChain;
    private IChain mTotalChain;

    private TextFactory() {
    }

    /**
     * get AndroidFactory object
     *
     * @return {@link SyntaxFactory}
     */
    public static SyntaxFactory create() {
        return new TextFactory();
    }

    @Override
    public Syntax getHorizontalRulesGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new HorizontalRulesSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getBlockQuotesGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new BlockQuotesSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getTodoGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new TodoSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getTodoDoneGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new TodoDoneSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getOrderListGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new OrderListSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getUnOrderListGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new UnOrderListSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getCenterAlignGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new CenterAlignSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getHeaderGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new HeaderSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getBoldGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new BoldSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getItalicGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new ItalicSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getInlineCodeGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new InlineCodeSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getStrikeThroughGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new StrikeThroughSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getFootnoteGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new FootnoteSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getImageGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new ImageSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getHyperLinkGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new HyperLinkSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getCodeGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new CodeSyntax(rxMDConfiguration);
    }

    @Override
    public Syntax getBackslashGrammar(@NonNull RxMDConfiguration rxMDConfiguration) {
        return new BackslashSyntax(rxMDConfiguration);
    }

    private void init(@NonNull RxMDConfiguration rxMDConfiguration) {
        mRxMDConfiguration = rxMDConfiguration;
        mTotalChain = new MultiGrammarsChain(
                getCodeGrammar(rxMDConfiguration),
                getUnOrderListGrammar(rxMDConfiguration),
                getOrderListGrammar(rxMDConfiguration));
        mLineChain = new GrammarSingleChain(getHorizontalRulesGrammar(rxMDConfiguration));
        GrammarDoElseChain blockQuitesChain = new GrammarDoElseChain(getBlockQuotesGrammar(rxMDConfiguration));
        GrammarDoElseChain todoChain = new GrammarDoElseChain(getTodoGrammar(rxMDConfiguration));
        GrammarDoElseChain todoDoneChain = new GrammarDoElseChain(getTodoDoneGrammar(rxMDConfiguration));
        GrammarMultiChains centerAlignChain = new GrammarMultiChains(getCenterAlignGrammar(rxMDConfiguration));
        GrammarMultiChains headerChain = new GrammarMultiChains(getHeaderGrammar(rxMDConfiguration));
        MultiGrammarsChain multiChain = new MultiGrammarsChain(
                getImageGrammar(rxMDConfiguration),
                getHyperLinkGrammar(rxMDConfiguration),
                getInlineCodeGrammar(rxMDConfiguration),
                getBoldGrammar(rxMDConfiguration),
                getItalicGrammar(rxMDConfiguration),
                getStrikeThroughGrammar(rxMDConfiguration),
                getFootnoteGrammar(rxMDConfiguration));
        GrammarSingleChain backslashChain = new GrammarSingleChain(getBackslashGrammar(rxMDConfiguration));

        mLineChain.setNextHandleGrammar(blockQuitesChain);

        blockQuitesChain.setNextHandleGrammar(todoChain);
        blockQuitesChain.addNextHandleGrammar(multiChain);

        todoChain.setNextHandleGrammar(todoDoneChain);
        todoChain.addNextHandleGrammar(multiChain);

        todoDoneChain.setNextHandleGrammar(centerAlignChain);
        todoDoneChain.addNextHandleGrammar(multiChain);

        centerAlignChain.addNextHandleGrammar(headerChain);
        centerAlignChain.addNextHandleGrammar(multiChain);

        multiChain.setNextHandleGrammar(backslashChain);
    }

    @NonNull
    @Override
    public CharSequence parse(@NonNull CharSequence charSequence, @NonNull RxMDConfiguration rxMDConfiguration) {
        if (rxMDConfiguration == null) {
            return charSequence;
        }
        if (mTotalChain == null || mLineChain == null || mRxMDConfiguration == null || mRxMDConfiguration != rxMDConfiguration) {
            init(rxMDConfiguration);
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(charSequence);
        ssb = parseTotal(mTotalChain, ssb);
        ssb = parseByLine(mLineChain, ssb);
        return ssb;
    }

    private SpannableStringBuilder parseTotal(IChain totalChain, SpannableStringBuilder ssb) {
        totalChain.handleGrammar(ssb);
        return ssb;
    }

    private SpannableStringBuilder parseByLine(IChain lineChain, SpannableStringBuilder content) {
        String text = content.toString();
        String[] lines = text.split("\n");
        SpannableStringBuilder[] ssbLines = new SpannableStringBuilder[lines.length];
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        int index = 0;
        for (int i = 0; i < lines.length; i++) {
            ssbLines[i] = (SpannableStringBuilder) content.subSequence(index, index + lines[i].length());
            lineChain.handleGrammar(ssbLines[i]);
            index += (lines[i]).length();
            if (i < lines.length - 1 || mRxMDConfiguration.isAppendNewlineAfterLastLine()) {
                ssbLines[i].append(NEWLINE);
                index += NEWLINE.length();
            }
            ssb.append(ssbLines[i]);
        }
        return ssb;
    }

}
