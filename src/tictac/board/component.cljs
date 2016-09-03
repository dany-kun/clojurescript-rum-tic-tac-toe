(ns tictac.board.component
  (:require [rum.core :as rum]
            [tictac.board.events :as event]
            [tictac.init :as setup]))

(def players-pieces {:player-1 "cross" :player-2 "circle"})

(rum/defc c-piece [player action]
  (let [draggable-class (when (:on-drag-start action) "draggable")
        classes {:class ["piece" draggable-class (player players-pieces)]}]
    [:span
     (merge classes action)
     (player setup/pieces)]))

(rum/defc c-box
  < {:key-fn (fn [_ _ i j] (str "col-" j "row-" i))}
  [state row-idx col-idx player]
  (let [coord [row-idx col-idx]
        fill-in-action (event/fill-box state coord)
        empty-class (when fill-in-action "empty")
        winner-class (when (= (deref setup/game-over) player) "winner")]
    [:div
     (merge {:class ["col" empty-class winner-class] :on-drag-over #(.preventDefault %)} fill-in-action)
     (c-piece player (event/clear-box state coord))]))


(rum/defc c-row
  < {:key-fn (fn [_ i _] (str "row-" i))}
  [state row-index row]
  [:div {:class "row"} (map-indexed (partial c-box state row-index) row)])

(rum/defc c-board < rum/reactive []
  (let [state (rum/react setup/game)
        board (:board state)]
    [:div {:class "game-container"}
     [:div {:class "board"} (map-indexed (partial c-row state) board)]
     [:div {:class "current-player"} (str "Current player: " ((:player state) setup/pieces))]]))
