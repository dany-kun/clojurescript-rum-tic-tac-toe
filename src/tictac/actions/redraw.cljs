(ns tictac.actions.redraw
  (:require [tictac.setup :refer [side]]))

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
       {:board board :player (switch-player player)})))
  ([{:keys [board player]} coord [src-r src-c]]
   (let [board-1 (assoc board src-r (assoc (get board src-r) src-c :empty))
         ] (update-board {:board board-1 :player player} coord))))
