(ns tetris-cljs.core
  (:require [tetris-cljs.graphics :as graphics]
            [tetris-cljs.input :as input]))

(enable-console-print!)

(graphics/draw :rect [20 20 100 150] (graphics/color 0 0 200 1) (graphics/color 0 0 0 1) 5)
(graphics/draw :text ["Hello World!" 10 15] (graphics/color 0 0 0 1) (graphics/color 0 0 0 0) 1)
