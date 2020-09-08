(ns post-it.handler.routes
  (:require [reitit.swagger :as swagger]))

(defn create
  [{:keys []}]
  ; @ TODO standartize middleware signatures
  [""
   ["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "Post It API documentation"}}
           :handler (swagger/create-swagger-handler)}}]

   ["/health"
    {:get {:summary   "Checks if the service is ready to process requests"
           :responses {200 {}}
           :handler   (constantly {:status 200})}}]])
