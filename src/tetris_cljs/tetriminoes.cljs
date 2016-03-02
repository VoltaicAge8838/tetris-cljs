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
      {:neighbors (apply hash-set (conj neighbors [0 -1]))
       :color color})))

(defn make-piece [color type & [xy]]
  {:color color
   :blocks (piece-types type)
   :xy (or xy [3 0])})

(defn get-xy [piece]
  (->> piece piece->coords (mapv #(mapv + (:xy piece) %))))

(defn can-add-piece [grid piece]
  (every?
    #(= {} (get-in grid %))
    (get-xy piece)))

(defn place-piece [grid piece]
  (reduce
    (fn [g cn] (assoc-in g (first cn) (last cn)))
    grid (map vector (get-xy piece) (piece->neighbors piece))))

(defn rotate-right [piece]
  (update piece :block
    #(vec (apply (partial map vector)
            (reverse %)))))

(defn rotate-left [piece]
  (update piece :block
    #(vec (reverse (apply (partial map vector) %)))))

(defn move-piece [piece xy-dif]
  (update piece :xy #(mapv + xy-dif %)))

(defn clear-lines [grid]
  (letfn [swap-xy [g] (apply (partial map vector) g)]
    (->> grid
         swap-xy
         (mapv #(if (every? (partial not= {}) %)
                  (vec (repeat {}))
                  %))
         swap-xy)))

; remove links to "cleared" lower neighbors
(defn cascade [grid])

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
