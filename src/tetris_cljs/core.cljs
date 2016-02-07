(ns tetris-cljs.core
  (:require [tetris-cljs.graphics :as graphics]
            [tetris-cljs.input :as input]))

(enable-console-print!)

(println "Hello world!" " I like toast!")

(graphics/draw :rect [20 20 100 150] (graphics/color 200 0 0 1) (graphics/color 0 50 0 1) 5)
(graphics/draw :text ["Hello World!" 10 15] (graphics/color 0 200 0 1) (graphics/color 0 0 0 1) 1)
