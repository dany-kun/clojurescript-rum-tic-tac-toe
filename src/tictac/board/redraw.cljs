(ns tictac.board.redraw
  (:require [tictac.init :refer [side pieces] :as setup]))

(defn winning-possibilities [board]
  (let [diag1 (range side)
        diag2 (reverse diag1)
        cols (for [col diag1] (repeat side col))]
    (concat
      board                                                 ;Rows
      (map #(mapv get board %)                              ;Diagonals and columns
           (conj cols diag1 diag2))))
  )

(defn winner? [{:keys [board player]}]
  (let [winning-strike (repeat side player)
        board-projections (winning-possibilities board)]
    (some #(= % winning-strike) board-projections)))

(defn switch-player [current-player]
  (if (= :player-1 current-player) :player-2 :player-1))


(defn- update-board [player [r c]]
  (fn [board]
    (let [row (get board r)]
      (assoc board r (assoc row c player)))))

(defn add-piece [player coord]
  (fn [old]
    (let [updated-state (update old :board (update-board player coord))]
      (if (winner? updated-state)
        (do
          (swap! setup/game-over (constantly player))
          updated-state)
        (update updated-state :player switch-player)))))

; Use spector?
(defn switch-pieces [player coord src]
  (fn [old]
    (-> old
        (update :board (update-board :empty src))
        ((add-piece player coord)))))

(defn update-board-state
  ([{:keys [player]} coord]
   (swap! setup/game (add-piece player coord)))
  ([{:keys [player]} coord src]
   (swap! setup/game (switch-pieces player coord src))))
