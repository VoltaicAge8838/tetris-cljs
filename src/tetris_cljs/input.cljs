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
        (swap! input-state (update-state-fn :key-down key-code (.-timeStamp e)))
        (println "key down " @input-state))))

(defn key-up [e]
  (do (swap! input-state (update-state-fn :key-up (.-keyCode e) (.-timeStamp e)))
      (println "key up " @input-state)))

(defn blur [e]
  (reset! input-state {:key-down {}, :key-up {}}))

(defn register-command [keyboard command-type key handler]
  (assoc-in keyboard [command-type key] handler))

(defn unregister-command [keyboard command-type key]
  (update keyboard command-type dissoc key))

(defn make-keyboard [timestamp keypresses]
  { :timestamp timestamp,
    :key-press keypresses})

(defn get-new-keypress [last-timestamp]
  (let [state @input-state]
    (->> state
      :key-down
      (map (fn [keypair]
              (->> keypair
                (filter #(> (% 1) last-timestamp))
                first)))
      (make-keyboard (:last-update state)))))

(.addEventListener js/window "keydown" key-down)
(.addEventListener js/window "keyup" key-up)
(.addEventListener js/window "blur" blur)
