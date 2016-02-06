(ns tetris-cljs.input)

; Source: http://stackoverflow.com/questions/1465374/javascript-event-keycode-constants
; also:   http://keycode.info/
(def key-event
  { 3 :cancel
    6 :help,
    8 :back-space,
    9 :tab,
    12 :clear,
    13 :return,
    14 :enter,
    16 :shift,
    17 :control,
    18 :alt,
    19 :pause,
    20 :caps-lock,
    27 :escape,
    32 :space,
    33 :page-up,
    34 :page-down,
    35 :end,
    36 :home,
    37 :left,
    38 :up,
    39 :right,
    40 :down,
    44 :print-screen,
    45 :insert,
    46 :delete,
    48 :0,
    49 :1,
    50 :2,
    51 :3,
    52 :4,
    53 :5,
    54 :6,
    55 :7,
    56 :8,
    57 :9,
    65 :a,
    66 :b,
    67 :c,
    68 :d,
    69 :e,
    70 :f,
    71 :g,
    72 :h,
    73 :i,
    74 :j,
    75 :k,
    76 :l,
    77 :m,
    78 :n,
    79 :o,
    80 :p,
    81 :q,
    82 :r,
    83 :s,
    84 :t,
    85 :u,
    86 :v,
    87 :w,
    88 :x,
    89 :y,
    90 :z,
    93 :context-menu,
    96 :numpad0,
    97 :numpad1,
    98 :numpad2,
    99 :numpad3,
    100 :numpad4,
    101 :numpad5,
    102 :numpad6,
    103 :numpad7,
    104 :numpad8,
    105 :numpad9,
    106 :multiply,
    107 :add,
    108 :separator,
    109 :subtract,
    110 :decimal,
    111 :divide,
    112 :F1,
    113 :F2,
    114 :F3,
    115 :F4,
    116 :F5,
    117 :F6,
    118 :F7,
    119 :F8,
    120 :F9,
    121 :F10,
    122 :F11,
    123 :F12,
    124 :F13,
    125 :F14,
    126 :F15,
    127 :F16,
    128 :F17,
    129 :F18,
    130 :F19,
    131 :F20,
    132 :F21,
    133 :F22,
    134 :F23,
    135 :F24,
    144 :num-lock,
    145 :scroll-lock,
    186 :semicolon,
    187 :equals,
    188 :comma,
    189 :dash,
    190 :period,
    191 :slash,
    192 :back-quote,
    219 :open-bracket,
    220 :back-slash,
    221 :close-bracket,
    222 :quote,
    224 :meta})

(def prevent-default-keys
  #{(key-event 9) ; tab
    (key-event 38) ; up
    (key-event 40) ; down
    (key-event 32) ; space
    (key-event 33) ; page-up
    (key-event 34) ; page-down
    (key-event 36) ; home
    (key-event 35)}) ; end

(def input-state (atom {:key-down {}, :key-up {}}))

(defn key-down [e]
  (let [key-code (.-keyCode e)]
    (do (when (prevent-default-keys key-code)
          (.preventDefault e))
        (swap! input-state #(assoc-in % [:key-down key-code] (.-timeStamp e)))
        (println "key down " (key-event key-code)))))

(defn key-up [e]
  (do (swap! input-state #(assoc-in % [:key-up (.-keyCode e)] (.-timeStamp e)))
      (println "key up " (.-keyCode e))))

(defn blur [e]
  (reset! input-state {:key-down {}, :key-up {}}))

(defn make-keyboard []
  { :keyDownHandlers {},
    :keyPressHandlers {}})

(defn register-command [keyboard command-type key handler]
  (assoc-in keyboard [command-type key] handler))

(defn unregister-command [keyboard command-type key]
  (update keyboard command-type dissoc key))

; (defn update-keyboard [keyboard elapsed-time])

(.addEventListener js/window "keydown" key-down)
(.addEventListener js/window "keyup" key-up)
(.addEventListener js/window "blur" blur)
