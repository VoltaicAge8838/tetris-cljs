(ns tetris-cljs.input)

; Source: http://stackoverflow.com/questions/1465374/javascript-event-keycode-constants
(def key-event
  { :cancel 3,
    :help 6,
    :back-space 8,
    :tab 9,
    :clear 12,
    :return 13,
    :enter 14,
    :shift 16,
    :control 17,
    :alt 18,
    :pause 19,
    :caps-lock 20,
    :escape 27,
    :space 32,
    :page-up 33,
    :page-down 34,
    :end 35,
    :home 36,
    :left 37,
    :up 38,
    :right 39,
    :down 40,
    :print-screen 44,
    :insert 45,
    :delete 46,
    :0 48,
    :1 49,
    :2 50,
    :3 51,
    :4 52,
    :5 53,
    :6 54,
    :7 55,
    :8 56,
    :9 57,
    :semicolon 59,
    :equals 61,
    :a 65,
    :b 66,
    :c 67,
    :d 68,
    :e 69,
    :f 70,
    :g 71,
    :h 72,
    :i 73,
    :j 74,
    :k 75,
    :l 76,
    :m 77,
    :n 78,
    :o 79,
    :p 80,
    :q 81,
    :r 82,
    :s 83,
    :t 84,
    :u 85,
    :v 86,
    :w 87,
    :x 88,
    :y 89,
    :z 90,
    :context-menu 93,
    :numpad0 96,
    :numpad1 97,
    :numpad2 98,
    :numpad3 99,
    :numpad4 100,
    :numpad5 101,
    :numpad6 102,
    :numpad7 103,
    :numpad8 104,
    :numpad9 105,
    :multiply 106,
    :add 107,
    :separator 108,
    :subtract 109,
    :decimal 110,
    :divide 111,
    :F1 112,
    :F2 113,
    :F3 114,
    :F4 115,
    :F5 116,
    :F6 117,
    :F7 118,
    :F8 119,
    :F9 120,
    :F10 121,
    :F11 122,
    :F12 123,
    :F13 124,
    :F14 125,
    :F15 126,
    :F16 127,
    :F17 128,
    :F18 129,
    :F19 130,
    :F20 131,
    :F21 132,
    :F22 133,
    :F23 134,
    :F24 135,
    :num-lock 144,
    :scroll-lock 145,
    :comma 188,
    :period 190,
    :slash 191,
    :back-quote 192,
    :open-bracket 219,
    :back-slash 220,
    :close-bracket 221,
    :quote 222,
    :meta 224})

(def- prevent-default-keys
  #{(:tab key-event)
    (:up key-event)
    (:down key-event)
    (:space key-event)
    (:page-up key-event)
    (:page-down key-event)
    (:home key-event)
    (:end key-event)})

(def- keys (atom {}))

(def- did-key-press (atom #{}))

(defn key-down [e]
  (do (when (prevent-default-keys (.keyCode e))
        (.preventDefault e))
      (swap! keys #(assoc % (.keyCode e) (.timeStamp e)))))

(defn key-up [e]
  (do (swap! keys #(dissoc % (.keyCode e)))
      (swap! did-key-press #(dissoc % (.keyCode e)))))

(defn blur [e]
  (reset! keys {}))

(defn make-keyboard []
  { :keyDownHandlers {},
    :keyPressHandlers {}})

(defn register-command [keyboard command-type key handler]
  (assoc-in keyboard [command-type key] handler))

(defn unregister-command [keyboard command-type key]
  (update keyboard command-type dissoc key))

(defn update [keyboard elapsed-time])
