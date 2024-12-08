from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('add_transaction.html/', views.add_transaction, name='add_transaction'),
    path('QueryResults.html', views.QueryResults, name='QueryResults'),
    path('BuyStocks.html', views.BuyStocks, name='BuyStocks'),
]
