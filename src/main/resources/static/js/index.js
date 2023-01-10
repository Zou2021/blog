var page = 0x1;
$(_0x85f81f => {
    $('.add>svg')['click'](_0xe77703 => {
        $('.add_box')['fadeIn'](0x190);
    });
    $('.add_tit>svg')['click'](_0x163488 => {
        $('.add_box')['fadeOut'](0x190);
    });
    $('.me_can')['click'](_0x105b5d => {
        $('.add_box')['fadeIn'](0x190);
    });
    push();
});

function get(_0x3a17d7) {
    let _0x8a59fd = 'https://wch666.com/api/b/haoju/get.php?fo=' + _0x3a17d7;
    $['get'](_0x8a59fd, _0x267b86 => {
        view(JSON['parse'](_0x267b86));
    });
}

function view(_0x8be45b) {
    let _0x3a9445 = '';
    for (let _0x48e1af = 0x0; _0x48e1af < _0x8be45b['length']; _0x48e1af++) {
        _0x3a9445 += '<div\x20class=\x22say_list\x22>\x0a\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20<div\x20class=\x22say\x22>' + _0x8be45b[_0x48e1af][0x0] + '</div>\x0a\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20</div>';
    }
    $('.say_box')['append'](_0x3a9445);
    $('.box_display')['fadeOut'](0x15e);
}

function push() {
    let _0x253e84 = $('.push')['0'];
    let _0x179d89 = new IntersectionObserver(_0x3d6e46 => {
        _0x3d6e46['forEach'](_0x5ccea8 => {
            if (_0x5ccea8['isIntersecting'] === !![]) {
                get(page);
                page += 0x1;
            }
        });
    });
    _0x179d89['observe'](_0x253e84);
}

$('.add_btn>span')['click'](_0x42a007 => {
    let _0x238c30 = $('textarea')['val']();
    if (_0x238c30['length'] < 0x1) {
        msg('请输入内容');
    } else {
        let _0x3c68f3 = 'https://wch666.com/api/b/haoju/push.php';
        $['post'](_0x3c68f3, {'nr': _0x238c30}, _0x552b6a => {
            if (_0x552b6a === 'ok') {
                let _0x34c88f = '<div\x20class=\x22say_list\x22>\x0a\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20<div\x20class=\x22say\x22>' + _0x238c30 + '</div>\x0a\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20\x20</div>';
                $('.say_box')['prepend'](_0x34c88f);
                msg('成功！');
                $('textarea')['val']('');
            }
        });
    }
});

function msg(_0xc2e18b) {
    ''['wch_msg'](_0xc2e18b);
}

$('.left_me_mod')['click'](_0x178acb => {
    let _0x343c7e = $('.left_me_mod')['attr']('data-v') / 0x1;
    if (_0x343c7e !== 0x1) {
        $('.left')['css']('height', '100%');
        $('.left_me_mod')['attr']('data-v', '1');
    } else {
        $('.left')['css']('height', '50px');
        $('.left_me_mod')['attr']('data-v', '0');
    }
});
let god = '';
$('.up_top_box')['click'](_0x5f4538 => {
    let _0x548af2 = $(document)['scrollTop']();
    let _0x1210c0 = _0x548af2 / 0x1e;
    god = setInterval(_0x26655a => {
        $(document)['scrollTop']($(document)['scrollTop']() - _0x1210c0);
        if ($(document)['scrollTop']() === 0x0) {
            clearInterval(god);
        }
    }, 0xa);
});