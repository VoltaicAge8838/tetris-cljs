(ns tetris-cljs.tetriminoes)

(def tiles-wide 10)
(def tiles-high 20)

(def new-grid
  (repeat tiles-wide
    (repeat tiles-high nil)))

;; To do: remove absolute height, width, etc.
;; these deal with graphics, not game logic
(defn get-block-width [width]
  (/ width tiles-wide))

(defn get-block-height [height]
  (/ height tiles-high))

(defn get-level-speed [lvl]
  (* 900 (- 1 (/ lvl (+ 2 lvl)))))

(def piece-types
  { :Z [[[1 1] [0 1] [0 0] [-1 0]]
        [[0 1] [0 0] [1 0] [1 -1]]]
    :S [[[-1 1] [0 1] [0 0] [1 0]]
        [[1 1] [0 0] [1 0] [0 -1]]]
    :O [[[0 1] [-1 1] [-1 0] [0 0]]]
    :I [[[-1 0] [0 0] [1 0] [-2 0]]
        [[0 2] [0 1] [0 0] [0 -1]]]
    :T [[[0 1] [-1 0] [0 0] [1 0]]
        [[0 1] [0 0] [-1 0] [0 -1]]
        [[-1 0] [0 0] [1 0] [0 -1]]
        [[0 1] [1 0] [0 0] [0 -1]]]
    :L [[[-1 1] [-1 0] [0 0] [1 0]]
        [[0 1] [0 0] [0 -1] [-1 -1]]
        [[-1 0] [0 0] [1 0] [1 -1]]
        [[1 1] [0 1] [0 0] [0 -1]]]
    :J [[[1 1] [-1 0] [0 0] [1 0]]
        [[0 1] [0 0] [-1 1] [0 -1]]
        [[-1 0] [0 0] [1 0] [-1 -1]]
        [[0 1] [0 0] [1 -1] [0 -1]]]})

(defn random-pieces-list []
  (shuffle (vals piece-types)))

(defn neighbor-direction [base n?]
  (let [dif [ (- (first base) (first n?))
              (- (last base) (last n?))]]
    (#{[0 -1] [0 1] [-1 0] [1 0]} dif)))

(defn compute-score [lines-cleared level]
  (apply * 50 (+ level 1) (range 2 lines-cleared)))

(defn make-blocks [coord-list color]
  (for [x coord-list]
    (for [y coord-list
          :let [dir (neighbor-direction x y)]
          :when dir]
      { :coord [x y]
        :block {:group dir
                :color color}})))

(defn make-piece [color type [rotation]]
  (let [p-type (piece-types type)]
    (->> p-type
      count
      (mod rotation)
      p-type
      make-blocks)))

(defn get-xy [x y block]
  (-> block :coords (map + [x y])))

(defn can-add-piece [grid x y piece]
  (every?
    #(get-in grid (get-xy x y %))
    piece))

(defn place-piece [grid x y piece]
  (when (can-add-piece grid x y piece)
    (reduce
      (fn [g block] (assoc-in g (get-xy x y block) (:block block)))
      grid piece)))

(defn make-game-state [color-list width height x-offset y-offset]
  { :level 0
    :score 0
    :mode :drop ;; modes: drop, cascade, clear, game-over, prompt-highscore
    :lines-cleared 0
    :grid (make-grid)
    :piece-queue (random-pieces-list)
    :active-block {}
    :next-block {}
    :level-speed 1
    :time 0
    :level-lines-cleared 0
    :total-lines-cleared 0
    :clear-line-counter 0
    :width width
    :height height
    :hard-drop false
    :soft-drop false})
