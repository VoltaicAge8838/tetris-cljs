(ns tetris_cljs.core
  (:require [tetris_cljs.graphics :as graphics]))

(enable-console-print!)

(println "Hello world!" " I like toast!")

(graphics/draw :rect [20 20 100 150] (graphics/color 200 0 0 1) (graphics/color 0 50 0 1) 5)
(graphics/draw :text ["Hello World!" 10 15] (graphics/color 0 200 0 1) (graphics/color 255 5 255 1) 1)
