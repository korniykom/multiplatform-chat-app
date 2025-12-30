local key = KEYS[1]
local maxRequests = tonumber(ARGV[1])
local ttl = tonumber(ARGV[2])

local current = redis.call('GET', key)
if current == false then
    redis.call('SET', key, 1, 'EX', ttl)
    return { 1, ttl}
else
    local count = tonumber(current)
    if count < maxRequests then
        local newCount = redis.call('INCR', key)
        local remainingTtl = redis.call('TTL', key)
        return {newCount, remainingTtl}
    else
       local remainingTtl = redis.call('TTL', key)
       return {count + 1, remainingTtl}
    end
end