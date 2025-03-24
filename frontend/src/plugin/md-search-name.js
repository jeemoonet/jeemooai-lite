export default function SearchPlugin(md) {
    // 支持两种格式的正则
    const citationRegex = /\[citation:(\d+)\]|\[([^\]]+)\]\(citation:(\d+)\)/;

    // 自定义行内解析规则
    function citationRule(state, silent) {
        if (silent) return false;

        const str = state.src.slice(state.pos);
        const match = citationRegex.exec(str);
        if (!match) return false;

        // 确保匹配从字符串起始位置开始
        if (match.index !== 0) return false;

        let citationNumber, citationText, isSimpleFormat;

        // 判断匹配的是哪种格式
        if (match[1]) {
            // 格式 [citation:数字]
            citationNumber = match[1];
            citationText = `[citation:${citationNumber}]`; // 默认文本
            isSimpleFormat = true; // 标记为简单格式
        } else if (match[2] && match[3]) {
            // 格式 [中国新闻网](citation:数字)
            citationText = match[2]; // 获取 [] 中的内容
            citationNumber = match[3]; // 获取 citation 后的数字
            isSimpleFormat = false; // 标记为非简单格式
        } else {
            return false; // 如果没有匹配到任何格式，返回 false
        }

        // 创建 token
        const token = state.push('citation', '', 0);
        token.attrs = [
            ['data-citation', citationNumber],
            ['data-text', citationText],
            ['data-format', isSimpleFormat ? 'simple' : 'text'] // 标记格式类型
        ];

        // 移动解析位置
        state.pos += match[0].length;
        return true;
    }

    // 注册行内规则
    md.inline.ruler.before('link', 'citation', citationRule);

    // 定义渲染规则
    md.renderer.rules.citation = function (tokens, idx) {
        const citationNumber = tokens[idx].attrs[0][1];
        const citationText = tokens[idx].attrs[1][1]; // 获取 [] 中的内容
        const formatType = tokens[idx].attrs[2][1]; // 获取格式类型
        const searchInfoData = md.options.searchInfoList[citationNumber - 1] || {};

        if (!searchInfoData.url) {
            return '';
        }

        if (formatType === 'simple') {
            // 格式 [citation:数字]
            return ` 
                <a href="${searchInfoData.url}" target="_blank" class="citation" 
                      data-citation="${citationNumber}"
                      data-title="${searchInfoData.title}"
                      data-desc="${searchInfoData.snippet}"
                      data-url="${searchInfoData.url}"
                      onmouseenter="__citationEvents.handleEnter(event)"
                      onmouseleave="__citationEvents.handleLeave(event)">
                    ${citationNumber}
                </a>
            `;
        } else {
            // 格式 [中国新闻网](citation:数字)
            return ` 
                <a href="${searchInfoData.url}" target="_blank">
                    ${citationText}
                </a>
            `;
        }
    };
}