(ns tetris-cljs.tetriminoes)

(def tiles-wide 10)
(def tiles-high 20)

(def empty-grid
  (vec (repeat tiles-wide
        (vec (repeat tiles-high {})))))

(defn get-level-speed [lvl]
  (* 900 (- 1 (/ lvl (+ 2 lvl)))))

(def piece-types
  { :z [[1 1 0]
        [0 1 1]]
    :s [[0 1 1]
        [1 1 0]]
    :o [[1 1]
        [1 1]]
    :i [[0 0 0 0]
        [1 1 1 1]
        [0 0 0 0]]
    :t [[0 0 0]
        [1 1 1]
        [0 1 0]]
    :l [[0 0 0]
        [1 1 1]
        [1 0 0]]
    :j [[0 0 0]
        [1 1 1]
        [0 0 1]]})

(defn random-pieces-list []
  (shuffle (vals piece-types)))

(defn compute-score [lines-cleared level]
  (apply * 50 (+ level 1) (range 2 lines-cleared)))

(defn neighbor-direction [base n?]
  (let [dif [ (- (first base) (first n?))
              (- (last base) (last n?))]]
    (#{[0 -1] [0 1] [-1 0] [1 0]} dif)))

(defn piece->coords [piece]
  (reduce-kv
    (fn [yo y row]
      (let [result (reduce-kv
                    (fn [xo x cell]
                      (cond-> xo
                        (= 1 cell) (conj [x y])))
                    [] row)]
        (cond-> yo
          (not-empty result) (into conj result))))
    [] (:blocks piece)))

(defn piece->neighbors [piece]
  (let [color (:color piece)
        coord-list (piece->coords piece)]
    (for [x coord-list
          :let [neighbors
                (for [y coord-list
                      :let [dir (neighbor-direction x y)]
                      :when dir]
                  dir)]]
      { :neighbors (apply hash-set (conj neighbors [0 -1]))
        :color color})))

(defn make-piece [color type]
  { :color color
    :blocks (piece-types type)})

(defn get-xy [x y piece]
  (->> piece piece->coords (mapv #(mapv + [x y] %))))

(defn can-add-piece [grid x y piece]
  (every?
    #(= {} (get-in grid %))
    (get-xy x y piece)))

(defn place-piece [grid x y piece]
  (reduce
    (fn [g cn] (assoc-in g (first cn) (last cn)))
    grid (map vector (get-xy x y piece) (piece->neighbors piece))))

(defn rotate-right [piece]
  (vec (apply (partial map vector)
          (reverse piece))))

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
