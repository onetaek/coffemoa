import { ref } from 'vue'

export const useResize = (props?: {
  minHeightPx?: number
  minWidthPx?: number
  initHeight?: number
  initWidth?: number
}) => {
  const {
    minHeightPx = 400,
    minWidthPx = window.innerWidth / 2,
    initHeight = 400,
    initWidth = window.innerWidth / 2
  } = props || {}
  // screen widas minimum width as minimum width
  //   const minWidthPx = window.innerWidth / 2
  // fixed minimum height 400px
  //   const minHeightPx = 400
  // The initial height limit is 400px
  const maxHeight = ref(initHeight + 'px')
  // The initial width is limited to 50%
  const minWidth = ref(initWidth + 'px')
  const setupDrag = (elDialog: any, el: any) => {
    // Get dialog element
    // Flag indicating whether resizing is in progress
    let isResizing = false
    // Current adjustment direction
    let currentResizeDirection = ''

    // Event handler when the mouse moves, used to detect the mouse position and set the corresponding cursor style
    const handleMouseMove = (e: any) => {
      const rect = elDialog.getBoundingClientRect()
      // The offset of the mouse relative to the left side of the dialog box
      const offsetX = e.clientX - rect.left
      // The offset of the mouse relative to the top of the dialog box
      const offsetY = e.clientY - rect.top
      const width = elDialog.clientWidth
      const height = elDialog.clientHeight

      // Get the padding of the dialog box
      const computedStyle = window.getComputedStyle(elDialog)
      const paddingLeft = parseFloat(computedStyle.paddingLeft)
      const paddingRight = parseFloat(computedStyle.paddingRight)
      const paddingBottom = parseFloat(computedStyle.paddingBottom)
      const paddingTop = parseFloat(computedStyle.paddingTop)

      // Set the corresponding cursor style and adjust the direction according to the mouse position
      if (!isResizing) {
        if (offsetX < paddingLeft && offsetY > paddingTop && offsetY < height - paddingBottom) {
          elDialog.style.cursor = 'ew-resize' // left and right arrows
          currentResizeDirection = 'left'
        } else if (
          offsetX > width - paddingRight &&
          offsetY > paddingTop &&
          offsetY < height - paddingBottom
        ) {
          elDialog.style.cursor = 'ew-resize' // left and right arrows
          currentResizeDirection = 'right'
        } else if (
          offsetY < paddingTop &&
          offsetX > paddingLeft &&
          offsetX < width - paddingRight
        ) {
          elDialog.style.cursor = 'ns-resize' // up and down arrows
          currentResizeDirection = 'top'
        } else if (
          offsetY > height - paddingBottom &&
          offsetX > paddingLeft &&
          offsetX < width - paddingRight
        ) {
          elDialog.style.cursor = 'ns-resize' // up and down arrows
          currentResizeDirection = 'bottom'
        } else if (offsetX < paddingLeft && offsetY < paddingTop) {
          elDialog.style.cursor = 'nwse-resize' // left up right down arrow
          currentResizeDirection = 'top-left'
        } else if (offsetX > width - paddingRight && offsetY < paddingTop) {
          elDialog.style.cursor = 'nesw-resize' // Right up left down arrow
          currentResizeDirection = 'top-right'
        } else if (offsetX < paddingLeft && offsetY > height - paddingBottom) {
          elDialog.style.cursor = 'nesw-resize' // Right up left down arrow
          currentResizeDirection = 'bottom-left'
        } else if (offsetX > width - paddingRight && offsetY > height - paddingBottom) {
          elDialog.style.cursor = 'nwse-resize' // left up right down arrow
          currentResizeDirection = 'bottom-right'
        } else {
          elDialog.style.cursor = 'default'
          currentResizeDirection = ''
        }
      }
    }

    // Event handler when mouse is pressed to start resizing the dialog box
    const handleMouseDown = (e) => {
      if (currentResizeDirection) {
        isResizing = true

        const initialX = e.clientX
        const initialY = e.clientY
        const initialWidth = elDialog.clientWidth
        const initialHeight = el.querySelector('.el-dialog__body').clientHeight

        // resize event handler
        const handleResizing = (e: any) => {
          if (!isResizing) return

          let newWidth = initialWidth
          let newHeight = initialHeight

          // Calculate new width and height based on current resize direction
          if (currentResizeDirection.includes('right')) {
            newWidth = Math.max(minWidthPx, initialWidth + (e.clientX - initialX) * 2)
            minWidth.value = `${newWidth}px`
          }

          if (currentResizeDirection.includes('left')) {
            newWidth = Math.max(minWidthPx, initialWidth - (e.clientX - initialX) * 2)
            minWidth.value = `${newWidth}px`
          }

          if (currentResizeDirection.includes('bottom')) {
            newHeight = Math.max(minHeightPx, initialHeight + (e.clientY - initialY) * 2 - 20)
            maxHeight.value = `${Math.min(newHeight, window.innerHeight - 165)}px`
          }

          if (currentResizeDirection.includes('top')) {
            newHeight = Math.max(minHeightPx, initialHeight - (e.clientY - initialY) * 2 - 20)
            maxHeight.value = `${Math.min(newHeight, window.innerHeight - 165)}px`
          }

          if (currentResizeDirection === 'top-left') {
            newWidth = Math.max(minWidthPx, initialWidth - (e.clientX - initialX) * 2)
            minWidth.value = `${newWidth}px`
            newHeight = Math.max(minHeightPx, initialHeight - (e.clientY - initialY) * 2 - 20)
            maxHeight.value = `${Math.min(newHeight, window.innerHeight - 165)}px`
          }

          if (currentResizeDirection === 'top-right') {
            newWidth = Math.max(minWidthPx, initialWidth + (e.clientX - initialX) * 2)
            minWidth.value = `${newWidth}px`
            newHeight = Math.max(minHeightPx, initialHeight - (e.clientY - initialY) * 2 - 20)
            maxHeight.value = `${Math.min(newHeight, window.innerHeight - 165)}px`
          }

          if (currentResizeDirection === 'bottom-left') {
            newWidth = Math.max(minWidthPx, initialWidth - (e.clientX - initialX) * 2)
            minWidth.value = `${newWidth}px`
            newHeight = Math.max(minHeightPx, initialHeight + (e.clientY - initialY) * 2 - 20)
            maxHeight.value = `${Math.min(newHeight, window.innerHeight - 165)}px`
          }

          if (currentResizeDirection === 'bottom-right') {
            newWidth = Math.max(minWidthPx, initialWidth + (e.clientX - initialX) * 2)
            minWidth.value = `${newWidth}px`
            newHeight = Math.max(minHeightPx, initialHeight + (e.clientY - initialY) * 2 - 20)
            maxHeight.value = `${Math.min(newHeight, window.innerHeight - 165)}px`
          }
        }
        // Stop resize event handler
        const stopResizing = () => {
          isResizing = false
          document.removeEventListener('mousemove', handleResizing)
          document.removeEventListener('mouseup', stopResizing)
        }

        document.addEventListener('mousemove', handleResizing)
        document.addEventListener('mouseup', stopResizing)
      }
    }
    elDialog.addEventListener('mousemove', handleMouseMove)
    elDialog.addEventListener('mousedown', handleMouseDown)
  }

  return {
    setupDrag,
    maxHeight,
    minWidth
  }
}
