(ns tetris-cljs.input
  (:require [tetris-cljs.key-codes :as key-codes]))

(def prevent-default-keys
  #{(:tab key-codes/key-to-code)
    (:up key-codes/key-to-code)
    (:down key-codes/key-to-code)
    (:space key-codes/key-to-code)
    (:page-up key-codes/key-to-code)
    (:page-down key-codes/key-to-code)
    (:home key-codes/key-to-code)
    (:end key-codes/key-to-code)})

(def input-state
  (atom { :key-down {},
          :key-up {}
          :last-update 0}))

(defn last-key-state [state code]
  (letfn [(get-time [keycode]
            (or (get-in state [keycode code]) 0))]
    (if (<= (get-time :key-down)
            (get-time :key-up))
        :key-down
        :key-up)))

(defn update-state-fn [type code timestamp]
  (fn [state]
    (if (= type (last-key-state state code))
      (-> state
        (assoc-in [type code] timestamp)
        (assoc :last-update timestamp))
      state)))

(defn key-down [e]
  (let [key-code (.-keyCode e)]
    (do (when (prevent-default-keys key-code)
          (.preventDefault e))
        (swap! input-state (update-state-fn :key-down key-code (.-timeStamp e))))))

(defn key-up [e]
  (swap! input-state (update-state-fn :key-up (.-keyCode e) (.-timeStamp e))))

(defn blur [e]
  (reset! input-state {:key-down {}, :key-up {}}))

(defn register-command [keyboard command-type key handler]
  (assoc-in keyboard [command-type key] handler))

(defn unregister-command [keyboard command-type key]
  (update keyboard command-type dissoc key))

(defn make-keyboard [timestamp keypresses]
  { :last-update timestamp,
    :key-press keypresses})

(defn cout [t x]
  (do (println t " debug: " x) x))

(defn get-new-keypresses [last-timestamp]
  (let [state @input-state]
    (->> state
      :key-down
      (map (fn [keypair]
              (->> keypair
                (filter #(> (% 1) last-timestamp))
                first)))
      (make-keyboard (:last-update state)))))

; game-state = {:last-update, :key-bindings {code fns}}
(defn process-input [game-state]
  (let [keypress-map (get-new-keypresses (:last-update game-state))
        new-game-state (assoc game-state :last-update (:last-update keypress-map))
        bindings (filter identity (map #(get (:key-bindings game-state) %)
                                      (:key-press keypress-map)))]
    (reduce #(if %2 (%2 %1) %1)
        new-game-state
        ((apply comp bindings) game-state))))

(defn init-key-listeners []
  (.addEventListener js/window "keydown" key-down)
  (.addEventListener js/window "keyup" key-up)
  (.addEventListener js/window "blur" blur))
