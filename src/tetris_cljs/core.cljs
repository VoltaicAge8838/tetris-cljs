(ns tetris_cljs.core
  (:require [tetris_cljs.graphics :as graphics]))

(enable-console-print!)

(println "Hello world!" " I like toast!")

(graphics/draw-rect-old 30 500 300 440)

(println (graphics/color 203 101 3 4))

(graphics/draw-rect [20 20 10 150] (graphics/color 200 0 0 1) (graphics/color 0 50 0 1) 5)
