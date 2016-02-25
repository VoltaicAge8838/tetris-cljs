(ns tetris-cljs.tetriminoes)

(def tiles-wide 10)
(def tiles-high 20)

(def empty-grid
  (vec (repeat tiles-wide
        (vec (repeat tiles-high {})))))

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

(defn piece->blocks [piece]
  (let [color (:color piece)
        coord-list (:coords piece)]
    (for [x coord-list
          :let [neighbors
                (for [y coord-list
                      :let [dir (neighbor-direction x y)]
                      :when dir
                      dir])]
          { :neighbors neighbors
            :color color}])))

(defn make-active-piece [color type coords]
  { :color color
    :type type
    :coords coords})

(defn make-piece [color type & [rotation]]
  (let [p-type (piece-types type)]
    (->> p-type
      count
      (mod (or rotation 0))
      p-type
      (make-active-piece color type))))

(defn get-xy [x y block]
  (-> block :coords (map + [x y])))

(defn can-add-piece [grid x y piece]
  (every?
    #(= {} (get-in grid (get-xy x y %)))
    piece))

(defn place-piece [grid x y piece]
  (reduce
    (fn [g block] (assoc-in g (get-xy x y block) (:block block)))
    grid piece))

(defn make-game-state [colors]
  { :level 0
    :score 0
    :mode :drop ;; modes: drop, cascade, clear, game-over, prompt-highscore
    :grid empty-grid
    :colors colors
    :piece-queue (random-pieces-list)
    :active-block nil
    :next-block nil
    :level-speed 1
    :time 0
    :lines-cleared 0
    :level-lines-cleared 0
    :total-lines-cleared 0
    :clear-line-counter 0
    :hard-drop false
    :soft-drop false})
