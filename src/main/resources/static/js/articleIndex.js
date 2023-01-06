/**
 * 获取iframe的dom操作对象
 *
 * @param id
 * @returns {Document|jQuery|HTMLElement|*}
 */
function getIFrameDOM(id) {
    return document.querySelector(id).contentDocument || document.frames[id].document;
}

/**
 * 使得iframe页面中class为heading的元素id为'heading'+i
 */
window.onload = function changeHeadingId() {
    let frameDOM = getIFrameDOM('#childFrame');
    let headingList = frameDOM.querySelectorAll('.heading');
    for (let i = 0; i < headingList.length; i++) {
        if (i === 0) {
            headingList[0].id = 'title';
        } else {
            headingList[i].id = 'heading' + i;
        }
    }

    createLinkToPath();//方法执行完成后调用生成目录索引标签
}

/**
 * 为heading插入a元素生成目录索引
 */
function createLinkToPath() {
    //获取iframe的DOM操作对象
    let frameDOM = getIFrameDOM('#childFrame');

    //查找id为title的标题，生成锚点链接
    let title = frameDOM.querySelector('#title');
    let div = document.querySelector('#articleTitle');

    //创建一个元素，设置值为title的值
    let titleLink = document.createElement('span');
    titleLink.textContent = title.textContent;
    titleLink.id = title.id;
    let titleClassList = titleLink.classList;//设置class属性
    titleClassList.add('lead');
    titleClassList.add('btn');
    titleClassList.add('border-danger');
    titleClassList.add('text-danger');
    div.appendChild(titleLink);

    //查找索引id为heading+i的元素，生成二级标题锚点链接
    let headingSize = frameDOM.querySelectorAll('.heading').length;//获取heading元素个数
    for (let i = 1; i < headingSize; i++) {
        //获取iframe元素与二级目录操作对象
        let heading = frameDOM.querySelector('#heading' + i);
        let articleHeading = document.querySelector('#articleHeading');

        //创建并初始化新元素
        let li = document.createElement('li');
        let link = document.createElement('span');
        link.textContent = heading.textContent;
        link.id = heading.id;
        let linkClasses = link.classList;
        linkClasses.add('lead');

        //插入元素
        li.appendChild(link);
        articleHeading.appendChild(li);
    }

    addEventToIndex();//为元素绑定事件
}

/**
 * 为索引链接元素绑定事件
 */
function addEventToIndex() {
    //获取iframe的DOM操作对象
    let frameDOM = getIFrameDOM('#childFrame');

    //绑定title点击事件
    document.querySelector('#title').addEventListener('click', function () {
        let top = frameDOM.querySelector("#title").offsetTop;
        window.scrollTo(0, top);
    });

    //绑定heading点击事件
    let headingSize = frameDOM.querySelectorAll('.heading').length;//获取heading元素个数
    for (let i = 1; i < headingSize; i++) {
        document.querySelector('#heading' + i).addEventListener('click', function () {
            let top = frameDOM.querySelector("#heading" + i).offsetTop;
            window.scrollTo(0, top);
        });
    }
}
