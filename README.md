# The Idea
It's confusing to read through IRC (Slack, Hipchat, etc.) scrollback and try to mentally piece together conversations.

Wouldn't it be nice to read discrete conversations instead?

# The Reality
Any realistic level of effort will produce results that are wrong some large percentage of the time! That's OK.

# The Goal

Given an IRC logfile parsed into `[<time> <speaker> <message>]`
triples, let's produce a data structure that
supports "conversational" printing as examined below.

### An easy goal

Luckily, text-mode communications tend to have simple addressing conventions.

It should be easy enough to turn this...

```
    08:45 Alice> Good morning!
    08:47 Brian> Alice: Good morning!
    08:52 Chuck> Anybody up for breakfast burritos?
    08:53 Daisy> Chuck: I love those
    08:54 Eddie> Alice: morning!
```

into something like this...

```
    # thread 1 -- started by Alice at 08:45
    08:45 Alice> Good morning!
    08:47 Brian> Alice: Good morning!
    08:54 Eddie> Alice: morning!

    # thread 2 -- started by Chuck at 08:52
    08:52 Chuck> Anybody up for breakfast burritos?
    08:53 Daisy> Chuck: I love those
```

### A less-easy goal

Another heuristic: one could judge thread participation by recency of
message, turning this...

```
    08:45 Alice> Good morning!
    08:47 Brian> Alice: Good morning!
    08:52 Chuck> Anybody up for breakfast burritos?
    08:53 Daisy> Chuck: I love those
    08:54 Eddie> Alice: morning!
    08:55 Daisy> Are they in the kitchen?
    08:56 Chuck> Daisy: yes
```

into this...

```
    # thread 1 -- started by Alice at 08:45
    08:45 Alice> Good morning!
    08:47 Brian> Alice: Good morning!
    08:54 Eddie> Alice: morning!

    # thread 2 -- started by Chuck at 08:52
    08:52 Chuck> Anybody up for breakfast burritos?
    08:53 Daisy> Chuck: I love those
    08:55 Daisy> Are they in the kitchen? ;; An unaddressed line in the middle of a conversation.
    08:55 Chuck> Daisy: yes
```

The distinction being that Daisy is still talking to Chuck, but
*without addressing her*. We can reasonably guess she's still speaking to
Chuck -- even though her last statement is unaddressed -- because
Chuck is addressing *her* shortly after.

There are likely other heuristics we can fall back on to increase accuracy.


# Provided bits

### Logfile parsing

- `judge-thredd.chat-logs` contains some utility logic to parse an IRC
logfile into a simple data structure useful as input to a solution
under development. Feel free to treat this namespace as a blackbox.

- `judge-thredd.-main` is, out of the box, **purely cosmetic**.
Currently it takes a filename representing a file present under
`resources/`, parses that file via `chat-logs/irc-log-messages` and
prints the results to STDOUT, like so...

```clojure
judge-thredd.core> (-main "easy-goal-example-irc-log-2015-08-07.txt")
:message-count 7
:first-10-messages
([<2015-08-07T08:45:00.000Z> "Alice" "Good morning!"]
[<2015-08-07T08:47:00.000Z> "Brian" "Alice: Good morning!"]
[<2015-08-07T08:52:00.000Z> "Chuck" "Anybody up for breakfast burritos?"]
[<2015-08-07T08:53:00.000Z> "Daisy" "Chuck: I love those"]
[<2015-08-07T08:54:00.000Z> "Eddie" "Alice: morning!"])
```


### IRC logs
The `resources/` directory contains some IRC logs.

`easy-goal-example-irc-log-2015-08-07.txt` is a logfile version of the 'Easy'
chat above. This should be useful for developing an algorithm against.

`clojure-irc-log-2015-08-07.txt` is an unadulterated sample of one
days' worth of irc.freenode.net/#clojure. This might be useful for
throwing a real-life set of conversations at a solution in order to
find edge cases.

# REPL

## deps.edn
`clj` or, e.g `clj -M:cider` to use your ~/.clojure/deps.edn's `cider` alias.

## lein

`lein repl`

# Testing

There is a failing unit test included as a starting point.

## deps.edn
`clj -X:test`

## lein
`lein test`
