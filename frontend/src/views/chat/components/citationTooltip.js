import { onMounted, onBeforeUnmount } from "vue";

export function useCitationTooltipController() {

    const PREVIEW_SIZE = { width: 420, height: 148 };
    let previewInstance = null;
    let transitionZone = null;
    let hideTimer = null;

    const startHideTimer = () => {
        hideTimer = setTimeout(removePreview, 300);
    };

    const clearHideTimer = () => {
        if (hideTimer) {
            clearTimeout(hideTimer);
            hideTimer = null;
        }
    };

    const citationEvents = {
        handleEnter: (e) => {
            clearHideTimer();
            const target = e.target.closest('.citation');
            if (!target) return;
            previewInstance = createPreviewElement(target.dataset);
            positionElements(target);

            previewInstance.addEventListener('mouseenter', clearHideTimer);
            previewInstance.addEventListener('mouseleave', (e) => {
                if (!e.relatedTarget?.closest('.citation, .preview-transition-zone')) {
                    startHideTimer();
                }
            });
        },

        handleLeave: (e) => {
            if (!e.relatedTarget?.closest('.citation-preview, .preview-transition-zone, .citation')) {
                startHideTimer();
            }
        }
    };

    const createPreviewElement = (dataset = {}) => {
        removePreview();

        const el = document.createElement('a');
        el.className = 'citation-preview';
        el.href = `${dataset.url}`; // 替换为实际链接
        el.target = '_blank';
        el.rel = 'noopener noreferrer';
        el.innerHTML = `<div class="citation-title">${dataset.title || '-'}</div>
                        <div class="citation-desc">${dataset.desc || '-'}</div>`;
        Object.assign(el.style, {
            width: `${PREVIEW_SIZE.width}px`,
            height: `${PREVIEW_SIZE.height}px`,
            position: 'absolute',
            pointerEvents: 'auto'
        });
        document.body.appendChild(el);
        return el;
    };

    const positionElements = (target) => {
        const targetRect = target.getBoundingClientRect();
        const viewport = {
            width: window.innerWidth,
            height: window.innerHeight,
            scrollY: window.scrollY
        };

        const pos = calculatePosition(targetRect, viewport);
        Object.assign(previewInstance.style, {
            left: `${pos.left}px`,
            top: `${pos.top}px`,
            opacity: '1'
        });

        createTransitionZone(targetRect, pos);
    };

    const calculatePosition = (targetRect, viewport) => {
        // 根据屏幕中线判断方向
        const isInUpperHalf = targetRect.top < viewport.height / 2;
        const position = isInUpperHalf ? 'below' : 'above';

        // 水平居中计算
        let left = targetRect.left + (targetRect.width - PREVIEW_SIZE.width) / 2;
        left = Math.max(10, Math.min(left, viewport.width - PREVIEW_SIZE.width - 10));

        // 垂直定位
        const top = position === 'below'
            ? targetRect.bottom + viewport.scrollY + 10  // 下方显示
            : targetRect.top + viewport.scrollY - PREVIEW_SIZE.height - 10;  // 上方显示

        // 边界保护
        const finalTop = position === 'below'
            ? Math.min(top, viewport.scrollY + viewport.height - PREVIEW_SIZE.height - 10)
            : Math.max(top, viewport.scrollY + 10);

        return { position, left, top: finalTop };
    };

    const createTransitionZone = (targetRect, previewPos) => {
        transitionZone = document.createElement('div');
        transitionZone.className = 'preview-transition-zone';

        const isBelow = previewPos.position === 'below';
        const targetBottom = targetRect.bottom + window.scrollY;
        const previewTop = previewPos.top;

        // 计算过渡区参数
        const zoneTop = isBelow ? targetBottom : previewTop + PREVIEW_SIZE.height;
        const zoneHeight = isBelow
            ? previewTop - targetBottom
            : (targetRect.top + window.scrollY) - (previewTop + PREVIEW_SIZE.height);

        Object.assign(transitionZone.style, {
            position: 'absolute',
            left: `${Math.min(targetRect.left, previewPos.left)}px`,
            width: `${Math.max(targetRect.width, PREVIEW_SIZE.width)}px`,
            top: `${zoneTop}px`,
            height: `${Math.max(zoneHeight, 0)}px`,
            pointerEvents: 'auto',
            zIndex: 9998
        });

        transitionZone.addEventListener('mouseenter', clearHideTimer);
        transitionZone.addEventListener('mouseleave', (e) => {
            if (!e.relatedTarget?.closest('.citation-preview, .citation')) {
                startHideTimer();
            }
        });
        document.body.appendChild(transitionZone);
    };

    const removePreview = () => {
        if (previewInstance) {
            previewInstance.removeEventListener('mouseenter', clearHideTimer);
            previewInstance.removeEventListener('mouseleave', startHideTimer);
            previewInstance.remove();
            previewInstance = null;
        }
        if (transitionZone) {
            transitionZone.remove();
            transitionZone = null;
        }
    };

    onMounted(() => {
        window.__citationEvents = citationEvents;
    });

    onBeforeUnmount(() => {
        removePreview();
        delete window.__citationEvents;
    });
}