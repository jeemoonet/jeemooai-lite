// import mermaid from 'mermaid'
export default function EchartsPlugin(md) {
  const temp = md.renderer.rules.fence.bind(md.renderer.rules)
  md.renderer.rules.fence = (tokens, idx, options, env, slf) => {
    const token = tokens[idx]
    if (token.info === 'echarts' || token.info === ' echars') {
      const code = token.content.trim()
      try {
        const json = JSON.parse(code)
        return `<div class="echarts"><pre class="echarts-data" data-json='${JSON.stringify(json)}'>${JSON.stringify(json)}</pre></div>`
      } catch (e) { // JSON.parse exception
        return `<pre class="hljs">${code}</pre>`
      }

    }
    return temp(tokens, idx, options, env, slf)
  }
}



