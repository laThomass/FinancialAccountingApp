DESIGN DESCRIPTION

We attempted to use the MVC pattern for this assignment as specified.

VIEW:
    The view outputs information to the user, and does not have any methods for
    calculation or input. It's only two methods are a method displaying the menu,
    and a method for displaying any string.

CONTROLLER:
    The controller has only the start method and a couple of helper methods to make
    it more readable and abstract certain functions. It takes information in, uses the model
    as needed, and outputs the result with the view.

MODEL:
    The model consists of the stock, portfolio, and alphaAPI classes and their respective
    interfaces. It receives calls from the controller to use its methods as needed, and
    sends its results back to the controller. The stock class is more accurately described as
    a data for a specific date for a specific ticker. AlphaAPI requests data from AlphaVantage,
    and returns a list of stock that is a list of the information of a stock for all dates
    in their system. The portfolio class represents a users collection of stocks and can
    store a list of stocks. In this case, a stock is represented by a ticker symbol and a
    quantity of shares. In order to perform operations with a stock, the api uses the symbol
    and grabs the data. To prevent unnecessary repeated calls, a stock symbol and its
    list of data, or List<Stock> is given to the library, which is initialized in the
    controller class upon startup.