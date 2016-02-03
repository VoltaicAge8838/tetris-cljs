(require 'cljs.build.api)

; to build on windows:
; java -cp "cljs.jar;src" clojure.main build.clj
; on linux:
; java -cp cljs.jar:src clojure.main build.clj

(cljs.build.api/build "src"
  { :main 'tetris_cljs.core
    :externs ["modernizr.js"]
    ; :optimizations :advanced
    :output-to "out/main.js"})
