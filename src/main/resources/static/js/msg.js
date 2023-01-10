/*! wch_msg | (c) JS A message plug-in | wch666.com/The author */
String['prototype']['wch_msg'] = function wch_msg(_0x1dd945) {
    if (typeof ''['wch_message_arr'] == 'undefined' || typeof ''['wch_message_class'] == 'undefined' || typeof ''['wch_message_top'] == 'undefined') {
        String['prototype']['wch_message_arr'] = [];
        String['prototype']['wch_message_class'] = 0x1869f;
        String['prototype']['wch_message_top'] = 0x0;
        let _0x21ef30 = document['createElement']('style');
        _0x21ef30['innerText'] = '\x20\x20.wch_message_jq_js\x20{\x0a\x20\x20\x20\x20\x20\x20min-width:\x20300px;\x0a\x20\x20\x20\x20\x20\x20box-sizing:\x20border-box;\x0a\x20\x20\x20\x20\x20\x20border-radius:\x204px;\x0a\x20\x20\x20\x20\x20\x20border:\x201px\x20solid\x20#28A745;\x0a\x20\x20\x20\x20\x20\x20position:\x20fixed;\x0a\x20\x20\x20\x20\x20\x20left:\x2050%;\x0a\x20\x20\x20\x20\x20\x20transform:\x20translateX(-50%);\x0a\x20\x20\x20\x20\x20\x20background-color:\x20#28A745;\x0a\x20\x20\x20\x20\x20\x20transition:\x20opacity\x20.3s,\x20transform\x20.4s,\x20top\x20.4s;\x0a\x20\x20\x20\x20\x20\x20box-shadow:\x200\x203px\x206px\x20-1px\x20rgb(0\x200\x200\x20/\x2012%),\x200\x2010px\x2036px\x20-4px\x20rgb(77\x2096\x20232\x20/\x2030%);\x0a\x20\x20\x20\x20\x20\x20overflow:\x20hidden;\x0a\x20\x20\x20\x20\x20\x20padding:\x2015px\x2015px\x2015px\x2020px;\x0a\x20\x20\x20\x20\x20\x20display:\x20flex;\x0a\x20\x20\x20\x20\x20\x20align-items:\x20center;\x0a\x20\x20\x20\x20\x20\x20color:\x20#fff;\x0a\x20\x20\x20\x20\x20\x20opacity:\x200;\x0a\x20\x20\x20\x20\x20\x20z-index:\x201;\x0a\x20\x20\x20\x20}';
        document['querySelector']('body')['appendChild'](_0x21ef30);
    }
    if (document['querySelectorAll']('.wch_message_jq_js')['length'] === 0x0) {
        String['prototype']['wch_message_top'] = -0x37;
    } else {
        let _0x5a522d = document['querySelector']('.wch_message_jq_js_' + ''['wch_message_arr'][''['wch_message_arr']['length'] - 0x1])['getAttribute']('data-top');
        String['prototype']['wch_message_top'] = _0x5a522d / 0x1;
    }
    String['prototype']['wch_message_class'] = ''['wch_message_class'] + 0x1;
    let _0x4e371f = document['createElement']('div');
    _0x4e371f['setAttribute']('style', 'z-index:' + ''['wch_message_class'] + ';top:' + ''['wch_message_top'] + 'px;');
    _0x4e371f['setAttribute']('data-top', '' + (''['wch_message_top'] + 0x50));
    _0x4e371f['setAttribute']('class', 'wch_message_jq_js_' + ''['wch_message_class'] + '\x20wch_message_jq_js');
    _0x4e371f['innerHTML'] = _0x1dd945;
    document['querySelector']('body')['appendChild'](_0x4e371f);
    ''['wch_message_arr']['push'](''['wch_message_class']);
    setTimeout(_0x1c2517 => {
        document['querySelector']('.wch_message_jq_js_' + ''['wch_message_class'])['style']['top'] = ''['wch_message_top'] + 0x50 + 'px';
        document['querySelector']('.wch_message_jq_js_' + ''['wch_message_class'])['style']['opacity'] = 0x1;
        setTimeout(_0x42848a => {
            for (let _0x58cfb4 = 0x0; _0x58cfb4 < ''['wch_message_arr']['length']; _0x58cfb4++) {
                let _0x5a0bb8 = document['querySelector']('.wch_message_jq_js_' + ''['wch_message_arr'][_0x58cfb4]);
                let _0xd42ad2 = '';
                if (_0x5a0bb8) {
                    _0xd42ad2 = _0x5a0bb8['getAttribute']('data-top') / 0x1 - 0x50;
                    _0x5a0bb8['setAttribute']('data-top', _0xd42ad2);
                    _0x5a0bb8['style']['top'] = _0xd42ad2 + 'px';
                }
                if (_0xd42ad2 === -0x37) {
                    setTimeout(_0x559ea7 => {
                        _0x5a0bb8['remove']();
                        if (document['querySelectorAll']('.wch_message_jq_js')['length'] === 0x0) {
                            String['prototype']['wch_message_top'] = 0x0;
                            String['prototype']['wch_message_arr'] = [];
                            String['prototype']['wch_message_class'] = 0x1869f;
                        }
                    }, 0x190);
                }
            }
        }, 0x7d0);
    }, 0x64);
};