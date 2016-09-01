(ns tictac.core
  (:require [rum.core :as rum]
            [cljs.reader :as reader]))

(enable-console-print!)

(def side 3)
(def board (vec (repeat side
                        (vec (repeat side :empty)))))
(def pieces {:player-1 "X" :player-2 "O" :empty "_"})
(def current-player :player-1)
(def state {:board board :player current-player})

(declare render!)

(defn can-move-piece [{:keys [board player]}]
  (let [all-places (flatten board)]
    (= side (count (filter #(= % player) all-places)))))

(defn winning-possibilities [board]
  (let [diag1 (range side)
        diag2 (reverse diag1)
        cols (for [col diag1] (repeat side col))]
    (concat
      board                                                 ;Rows
      (map #(mapv get board %)                              ;Diagonals and columns
           (conj cols diag1 diag2))))
  )

(defn has-won? [board player]
  (let [winning-strike (repeat side player)
        board-projections (winning-possibilities board)]
    (some #(= % winning-strike) board-projections)))

(defn switch-player [current-player]
  (if (= :player-1 current-player) :player-2 :player-1))

; Use spector?
(defn update-board
  ([{:keys [board player]} [r c]]
   (let [row (get board r)
         board (assoc board r (assoc row c player))]
     (if (has-won? board player)
       (js/alert "Player won")
       (render! {:board board :player (switch-player player)})
       )))
  ([{:keys [board player]} coord [src-r src-c]]
   (let [board-1 (assoc board src-r (assoc (get board src-r) src-c :empty))
         ] (update-board {:board board-1 :player player} coord))))

(defn store-drag-position [[r c]]
  #(.setData (.-dataTransfer %) "text" (str [r c])))

(defn empty-spot? [board coord]
  (= :empty (get-in board coord)))

(def retrieve-drag-position
  #(reader/read-string (.getData (.-dataTransfer %) "text")))

(defn drop-allowed? [board src target]
  (and (empty-spot? board target)
       (every? #(< (js/Math.abs %) 2) (map - src target))))

(defn on-element-drop [{:keys [board] :as state} coord]
  (fn [e]
    (let [src (retrieve-drag-position e)
          drop-allowed (drop-allowed? board src coord)]
      (when drop-allowed
        (update-board state coord src)))))

(defn on-box-clicked [{:keys [board] :as state} coord]
  #(when (empty-spot? board coord)
    (update-board state coord)))

(defn box-action [{:keys [board player] :as state} board-full? coord]
  (if board-full?
    (cond
      (empty-spot? board coord) {:on-drop (on-element-drop state coord)}
      (= player (get-in board coord)) {:on-drag-start (store-drag-position coord) :draggable true})
    {:on-click (on-box-clicked state coord)}))


(rum/defc render-piece
  < {:key-fn (fn [_ _ i j] (str "col-" j "row-" i))}
  [state row-idx col-idx col]
  (let [coord [row-idx col-idx]
        board-full? (can-move-piece state)
        action (box-action state board-full? coord)]
    [:div
     (merge {:class "col" :on-drag-over #(.preventDefault %)} action)
     (col pieces)]))

(rum/defc render-row
  < {:key-fn (fn [_ i _] (str "row-" i))}
  [state row-index row]
  [:div {:class "row"} (map-indexed (partial render-piece state row-index) row)])

(rum/defc tic-tac-board [{:keys [board player] :as state}]
  [:div (map-indexed (partial render-row (assoc state :player player)) board)])


(defn render! [state]
  (rum/mount (tic-tac-board state) (js/document.getElementById "board-container")))

(render! state)