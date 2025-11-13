import * as monaco from 'monaco-editor'
import { ref, nextTick, onBeforeUnmount } from 'vue'
import editorWorker from 'monaco-editor/esm/vs/editor/editor.worker?worker'
import jsonWorker from 'monaco-editor/esm/vs/language/json/json.worker?worker'
import cssWorker from 'monaco-editor/esm/vs/language/css/css.worker?worker'
import htmlWorker from 'monaco-editor/esm/vs/language/html/html.worker?worker'
import tsWorker from 'monaco-editor/esm/vs/language/typescript/ts.worker?worker'

self.MonacoEnvironment = {
  getWorker(_, label) {
    if (label === 'json') {
      return new jsonWorker()
    }
    if (label === 'css' || label === 'scss' || label === 'less') {
      return new cssWorker()
    }
    if (label === 'html' || label === 'handlebars' || label === 'razor') {
      return new htmlWorker()
    }
    if (label === 'typescript' || label === 'javascript') {
      return new tsWorker()
    }
    return new editorWorker()
  }
}

export function useMonacoEditor(language: string = 'javascript') {
  // Editor example
  let monacoEditor: monaco.editor.IStandaloneCodeEditor | null = null
  // target element
  const monacoEditorRef = ref<HTMLElement>()

  // Create instance
  function createEditor(editorOption: monaco.editor.IStandaloneEditorConstructionOptions = {}) {
    if (!monacoEditorRef.value) return
    monacoEditor = monaco.editor.create(monacoEditorRef.value, {
      // initial model
      model: monaco.editor.createModel('', language),
      // Whether to enable preview images
      minimap: { enabled: true },
      // rounded corners
      roundedSelection: true,
      // theme
      theme: 'vs-dark',
      // primary key
      multiCursorModifier: 'ctrlCmd',
      // scroll bar
      scrollbar: {
        verticalScrollbarSize: 8,
        horizontalScrollbarSize: 8
      },
      // Line number
      lineNumbers: 'on',
      // tab size
      tabSize: 2,
      //font size
      fontSize: 14,
      // Controls whether the editor should automatically adjust indentation when the user types, pastes, moves, or indents a line
      autoIndent: 'advanced',
      // autolayout
      automaticLayout: true,
      ...editorOption
    })
    return monacoEditor
  }

  // format
  async function formatDoc() {
    await monacoEditor?.getAction('editor.action.formatDocument')?.run()
  }

  // Data update
  function updateVal(val: string) {
    nextTick(() => {
      if (getOption(monaco.editor.EditorOption.readOnly)) {
        updateOptions({ readOnly: false })
      }
      monacoEditor?.setValue(val)
      setTimeout(async () => {
        await formatDoc()
      }, 10)
    })
  }

  // Configuration update
  function updateOptions(opt: monaco.editor.IStandaloneEditorConstructionOptions) {
    monacoEditor?.updateOptions(opt)
  }

  // Get configuration
  function getOption(name: monaco.editor.EditorOption) {
    return monacoEditor?.getOption(name)
  }

  // Get instance
  function getEditor() {
    return monacoEditor
  }

  function changeLanguage(newLanguage: string) {
    const model = monacoEditor?.getModel()
    if (model) {
      monaco.editor.setModelLanguage(model, newLanguage)
    }
  }

  function changeTheme(newTheme: string) {
    monaco.editor.setTheme(newTheme)
  }

  // Page left destroyed
  onBeforeUnmount(() => {
    if (monacoEditor) {
      monacoEditor.dispose()
    }
  })

  return {
    monacoEditorRef,
    createEditor,
    getEditor,
    updateVal,
    updateOptions,
    getOption,
    formatDoc,
    changeLanguage,
    changeTheme
  }
}
