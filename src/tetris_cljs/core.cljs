(ns tetris-cljs.core
  (:require [tetris-cljs.graphics :as graphics]
            [tetris-cljs.input :as input]))

(enable-console-print!)

(defn cout [t x]
  (do (println t " debug: " x) x))

(graphics/draw :rect [20 20 100 150] (graphics/color 200 0 0 1) (graphics/color 0 0 0 1) 5)
(graphics/draw :text ["Hello World!" 10 15] (graphics/color 0 100 0 1) (graphics/color 0 0 0 0) 1)

(def game-state {:last-update 123456789, :key-bindings {32 #(assoc % :five 5) 25 #(assoc % :four 5)  111 #(assoc % :three? 5)}})
(def keypress-map {:last-update 123456790, :key-press '(32 25 64)})
; (println (input/process-input game-state))
; (println "let" (input/get-new-keypresses (:last-update game-state)))
; (println "assoc" (assoc game-state :last-update (:last-update keypress-map)))
(def funcs (filter identity (map #(get (:key-bindings game-state) %) (:key-press keypress-map))))
(println "map" ((apply comp funcs) game-state))
; (input/init-key-listeners)
