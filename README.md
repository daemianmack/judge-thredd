# The Idea

It's confusing to read through IRC (Slack, Hipchat, etc.) scrollback and try to mentally piece together conversations.

Wouldn't it be nice to read discrete conversations instead?

*NB: Any realistic level of effort will produce results that are wrong some large percentage of the time! That's OK.*


## Easy

Luckily text-mode communications tend to have simple attribution conventions.

It should be easy enough to turn this...

```
    08:45 Alice> Good morning!
    08:47 Brian> Alice: Good morning!
    08:52 Chuck> Anybody up for breakfast burritos?
    08:53 Daisy> Chuck: I love breakfast burritos
    08:54 Eddie> Alice: morning!
```

into this...

```
    08:45 Alice> Good morning!
    08:47 Brian> Alice: Good morning!
    08:54 Eddie> Alice: morning!

    08:52 Chuck> Anybody up for breakfast burritos?
    08:53 Daisy> Chuck: I love breakfast burritos
```

## Less easy

Judging thread participation by recency of message, turning this...

```
    08:45 Alice> Good morning!
    08:47 Brian> Alice: Good morning!
    08:52 Chuck> Anybody up for breakfast burritos?
    08:53 Daisy> Chuck: I love breakfast burritos
    08:54 Eddie> Alice: morning!
    08:55 Daisy> Are they in the kitchen?
    08:56 Chuck> Daisy: yes
```

into this...

```
    08:45 Alice> Good morning!
    08:47 Brian> Alice: Good morning!
    08:54 Eddie> Alice: morning!

    08:52 Chuck> Anybody up for breakfast burritos?
    08:53 Daisy> Chuck: I love breakfast burritos
    08:55 Daisy> Are they in the kitchen? ;; An unattributed line in the middle of a conversation.
    08:55 Chuck> Daisy: yes
```

The distinction here being that Daisy is continuing talking to Chuck
but without attribution. We can reasonably guess she's still speaking
to Chuck even though her last statement is unattributed because Chuck
is attributing *her* shortly after.

There are likely other heuristics we can fall back on to increase accuracy!


# Existing bits

## Main interface

CLI: `lein run example-chat-2015-08-07.txt`

REPL: `(judge-thredd.core/-main example-chat-2015-08-07.txt)`


## Logfile parsing

`judge-thredd.chat-logs` contains some utility logic to parse an IRC
logfile into a simple data structure useful as input to a solution
under development. It should be possible to treat this namespace as a
blackbox.

`judge-thredd.-main` takes a filename and uses that utility logic as a
helpful starting point, printing to the console a portion of the
results for orientation...

```
judge-thredd.core> (-main "example-chat-2015-08-07.txt")
:message-count 7
:first-10-messages
([<2015-08-07T08:45:00.000Z> "Alice" "Good morning!"]
[<2015-08-07T08:47:00.000Z> "Brian" "Alice: Good morning!"]
[<2015-08-07T08:52:00.000Z> "Chuck" "Anybody up for breakfast burritos?"]
[<2015-08-07T08:53:00.000Z> "Daisy" "Chuck: I love breakfast burritos"]
[<2015-08-07T08:54:00.000Z> "Eddie" "Alice: morning!"]
[<2015-08-07T08:55:00.000Z> "Daisy" "Are they in the kitchen?"]
[<2015-08-07T08:56:00.000Z> "Chuck" "Daisy: yes"])
=> nil
judge-thredd.core>
```


## Example IRC logs
The `resources` directory contains some sample IRC logs.

`example-chat-2015-08-07.txt` is a logfile version of the 'Easy' chat
above. This should be useful for developing an algorithm against.

`clojure-2015-08-07.txt` is an unadulterated sample of one days' worth
of irc.freenode.net/#clojure. This might be useful for throwing a
real-life "in the wild" set of conversations at a solution to find
edge cases.


# Testing

There is a broken unit test included as a means of further
illustrating what one possible approach might produce.
