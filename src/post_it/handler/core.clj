(ns post-it.handler.core
  (:require [integrant.core :as ig]
            [muuntaja.core :as m]
            reitit.coercion.spec
            [reitit.dev.pretty :as pretty]
            [reitit.ring :as ring]
            [reitit.ring.coercion :as coercion]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.spec :as spec]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [spec-tools.spell :as spell]
            [post-it.handler.routes :as routes]))

(defmethod ig/init-key ::handler [_ deps]
  (ring/ring-handler
   (ring/router
    (routes/create deps)
    {:validate spec/validate
     :reitit.spec/wrap spell/closed
     :exception pretty/exception
     :data      {:coercion   reitit.coercion.spec/coercion
                 :muuntaja   m/instance
                 :middleware [swagger/swagger-feature
                              parameters/parameters-middleware
                              muuntaja/format-negotiate-middleware
                              muuntaja/format-response-middleware
                              exception/exception-middleware
                              muuntaja/format-request-middleware
                              coercion/coerce-response-middleware
                              coercion/coerce-request-middleware]}})
   (->> (ring/create-default-handler)
        (ring/routes
         (swagger-ui/create-swagger-ui-handler
          {:path   "/"
           :url    "/swagger.json"
           :config {:validatorUrl     nil
                    :operationsSorter "alpha"}})))))
