(ns tictac.init)

(def side 3)
(def pieces {:player-1 "X" :player-2 "O" :empty ""})
(def board (vec (repeat side
                        (vec (repeat side :empty)))))

(def current-player :player-1)
(def initial-game {:board board :player current-player})
(def game (atom initial-game))

(def game-over (atom nil))

(defn restart! []
  (reset! game initial-game)
  (reset! game-over nil))