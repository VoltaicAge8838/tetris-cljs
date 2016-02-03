(require 'cljs.build.api)

(cljs.build.api/watch "src"
  { :main 'tetris_cljs.core
    :output-to "out/main.js"})
