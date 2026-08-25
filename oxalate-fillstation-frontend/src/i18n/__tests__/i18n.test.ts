import i18n from '../index';

describe('internationalization', () => {
    test.each(['en', 'de', 'es', 'fi', 'sv'])('loads_%s_translation', (language) => {
        expect(i18n.getResourceBundle(language, 'translation'))
            .toBeTruthy();
    });

    test('unknownLanguage_fallsBackToEnglish', async () => {
        await i18n.changeLanguage('xx');
        expect(i18n.language)
            .toBe('xx');
        expect(i18n.t('auth.login'))
            .toBe(i18n.getResource('en', 'translation', 'auth.login'));
        await i18n.changeLanguage('en');
    });
});
